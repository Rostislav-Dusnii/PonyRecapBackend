package ucll.be.domain;

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
import ucll.be.model.Address;
import ucll.be.model.Animal;
import ucll.be.model.DomainException;
import ucll.be.model.Stable;
import ucll.be.utilits.TimeTracker;

public class StableTest {

    public static final String DEFAULT_NAME = "Default Name";
    public static final int DEFAULT_MAX_ANIMALS = 1;

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
    public void givenValidStableData_whenCreatingStable_thenNoValidationErrors() {
        // Given
        String name = "Valid Name";
        int maxAnimals = 2;

        // When
        Stable stable = new Stable(name, maxAnimals);

        // Then
        Set<ConstraintViolation<Stable>> violations = validator.validate(stable);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void givenBlankName_whenCreatingStable_thenValidationErrors() {
        // Given
        String name = "";
        int maxAnimals = 2;

        // When
        Stable stable = new Stable(name, maxAnimals);

        // Then
        Set<ConstraintViolation<Stable>> violations = validator.validate(stable);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Stable> violation = violations.iterator().next();
        Assertions.assertEquals("Name is mandatory", violation.getMessage());
    }

    @Test
    public void givenNegativeMaxAnimals_whenCreatingStable_thenValidationErrors() {
        // Given
        String name = "Valid Name";
        int maxAnimals = -2;

        // When
        Stable stable = new Stable(name, maxAnimals);

        // Then
        Set<ConstraintViolation<Stable>> violations = validator.validate(stable);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Stable> violation = violations.iterator().next();
        Assertions.assertEquals("Max animals must be positive", violation.getMessage());
    }

    @Test
    public void givenStableWithoutAddress_whenAddingAddress_thenAddressIsAdded() {
        // Given
        Stable stable = createDefaultStable();
        Address address = AddressTest.createDefaultAddress();

        // When
        stable.setAddress(address);

        // Then
        Assertions.assertEquals(address, stable.getAddress());
    }

    @Test
    public void givenStableWithAddress_whenAddingAddress_thenDomainExceptionThrown() {
        // Given
        Stable stable = createDefaultStable();
        Address address = AddressTest.createDefaultAddress();
        stable.setAddress(address);
        Address newAddress = AddressTest.createDefaultAddress();

        // When
        Exception exception = Assertions.assertThrows(
            DomainException.class, () -> stable.setAddress(newAddress)
        );

        // Then
        String expectedMessage = "Address is already set for " + stable.getName() + " stable";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    // GivenStableWithMaxNumberOfAnimals_WhenAddingAnAnimal_thenDomainExceptionIsThrown
    @Test
    public void givenStableWithMaxNumberOfAnimals_whenAddingAnAnimal_thenDomainExceptionIsThrown() {
        // Given
        Stable stable = createDefaultStable();
        stable.addAnimal(AnimalTest.createDefaultPony());
        Animal animal = AnimalTest.createDefaultPony();
        animal.setName("Another Animal");

        // When
        Exception exception = Assertions.assertThrows(
            DomainException.class, () -> stable.addAnimal(animal)
        );

        // Then
        String expectedMessage = "Stable with name " + stable.getName() + " is full";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    public static Stable createDefaultStable() {
        return new Stable(DEFAULT_NAME, DEFAULT_MAX_ANIMALS);
    }
}
