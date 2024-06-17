package ucll.be.domain;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import ucll.be.model.Animal;
import ucll.be.model.DomainException;
import ucll.be.model.MedicalRecord;
import ucll.be.utilits.TimeTracker;

public class MedicalRecordTest {
    public static final String DEFAULT_DESCRIPTION = "Default Description";
    public static final LocalDate DEFAULT_TODAY = LocalDate.of(2021, 1, 1);
    public static final LocalDate DEFAULT_YESTERDAY = LocalDate.of(2020, 12, 31);
    public static final LocalDate DEFAULT_TOMORROW = LocalDate.of(2021, 1, 2);

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void InitializeValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void CleanUpValidator() {
        validatorFactory.close();
    }

    @BeforeEach
    public void resetTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
    }

    @Test
    public void givenValidMedicalRecordData_whenCreatingMedicalRecord_thenNoValidationErrors() {
        // Given
        TimeTracker.setCustomToday(DEFAULT_TODAY);
        LocalDate registrationDate = DEFAULT_TODAY;
        String description = DEFAULT_DESCRIPTION;
        Animal animal = AnimalTest.createDefaultPony();

        // When
        MedicalRecord medicalRecord = new MedicalRecord(registrationDate, description, animal);

        // Then
        Set<ConstraintViolation<MedicalRecord>> violations = validator.validate(medicalRecord);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void givenBlankDescription_whenCreatingMedicalRecord_thenValidationErrors() {
        // Given
        TimeTracker.setCustomToday(DEFAULT_TODAY);
        LocalDate registrationDate = DEFAULT_TODAY;
        String description = "";
        Animal animal = AnimalTest.createDefaultPony();

        // When
        MedicalRecord medicalRecord = new MedicalRecord(registrationDate, description, animal);

        // Then
        Set<ConstraintViolation<MedicalRecord>> violations = validator.validate(medicalRecord);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void givenRegistrationDateInFuture_whenCreatingMedicalRecord_thenDomainExceptionThrown() {
        // Given
        TimeTracker.setCustomToday(DEFAULT_TODAY);
        LocalDate registrationDate = DEFAULT_TOMORROW;
        String description = DEFAULT_DESCRIPTION;
        Animal animal = AnimalTest.createDefaultPony();

        // When
        Exception exception = Assertions.assertThrows(
            DomainException.class, () -> new MedicalRecord(registrationDate, description, animal)
        );

        // Then
        String expectedMessage = "Registration date cannot be in the future";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenClosingDateBeforeRegistrationDate_whenCreatingMedicalRecord_thenDomainExceptionThrown() {
        // Given
        TimeTracker.setCustomToday(DEFAULT_TODAY);
        LocalDate registrationDate = DEFAULT_TODAY;
        LocalDate closingDate = DEFAULT_YESTERDAY;
        String description = DEFAULT_DESCRIPTION;
        Animal animal = AnimalTest.createDefaultPony();
        MedicalRecord medicalRecord = new MedicalRecord(registrationDate, description, animal);

        // When
        Exception exception = Assertions.assertThrows(
            DomainException.class, () -> medicalRecord.setClosingDate(closingDate)
        );

        // Then
        String expectedMessage = "Closing date must be at least 1 day after registration date";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenNullAnimal_whenCreatingMedicalRecord_thenValidationErrors() {
        // Given
        TimeTracker.setCustomToday(DEFAULT_TODAY);
        LocalDate registrationDate = DEFAULT_TODAY;
        String description = DEFAULT_DESCRIPTION;
        Animal animal = null;

        // When
        MedicalRecord medicalRecord = new MedicalRecord(registrationDate, description, animal);

        // Then
        Set<ConstraintViolation<MedicalRecord>> violations = validator.validate(medicalRecord);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void givenMedicalRecordWithAnimal_whenAddingAnimalToMedicalRecord_thenAnimalUpdated() {
        // Given
        TimeTracker.setCustomToday(DEFAULT_TODAY);
        LocalDate registrationDate = DEFAULT_TODAY;
        String description = DEFAULT_DESCRIPTION;
        Animal animal = AnimalTest.createDefaultPony();
        MedicalRecord medicalRecord = new MedicalRecord(registrationDate, description, animal);
        Animal newAnimal = AnimalTest.createDefaultPony();

        // When
        medicalRecord.setAnimal(newAnimal);

        // Then
        Assertions.assertEquals(newAnimal, medicalRecord.getAnimal());
    }

    public static MedicalRecord createDefaultMedicalRecord() {
        return new MedicalRecord(DEFAULT_TODAY, DEFAULT_DESCRIPTION, AnimalTest.createDefaultPony());
    }
}
