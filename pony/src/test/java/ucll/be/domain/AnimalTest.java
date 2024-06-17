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
import ucll.be.model.Animal;
import ucll.be.model.DomainException;
import ucll.be.model.Pony;
import ucll.be.model.Stable;
import ucll.be.utilits.TimeTracker;

public class AnimalTest {
    public static final String DEFAULT_NAME = "Default Name";
    public static final int DEFAULT_AGE = 1;

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
    public void givenValidAnimalData_whenCreatingAnimal_thenNoValidationErrors() {
        // Given
        String name = "Valid Name";
        int age = 2;

        // When
        Animal animal = new Pony(name, age);

        // Then
        Set<ConstraintViolation<Animal>> violations = validator.validate(animal);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void givenBlankName_whenCreatingAnimal_thenValidationErrors() {
        // Given
        String name = "";
        int age = 2;

        // When
        Animal animal = new Pony(name, age);

        // Then
        Set<ConstraintViolation<Animal>> violations = validator.validate(animal);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Animal> violation = violations.iterator().next();
        Assertions.assertEquals("Name is required", violation.getMessage());
    }

    @Test
    public void givenNegativeAge_whenCreatingAnimal_thenValidationErrors() {
        // Given
        String name = "Valid Name";
        int age = -2;

        // When
        Animal animal = new Pony(name, age);

        // Then
        Set<ConstraintViolation<Animal>> violations = validator.validate(animal);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Animal> violation = violations.iterator().next();
        Assertions.assertEquals("Age must be greater than or equal to 1", violation.getMessage());
    }

    @Test
    public void givenAgeLessThanOne_whenCreatingAnimal_thenValidationErrors() {
        // Given
        String name = "Valid Name";
        int age = 0;

        // When
        Animal animal = new Pony(name, age);

        // Then
        Set<ConstraintViolation<Animal>> violations = validator.validate(animal);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Animal> violation = violations.iterator().next();
        Assertions.assertEquals("Age must be greater than or equal to 1", violation.getMessage());
    }

    @Test
    public void givenAgeGreaterThanFiftyOne_whenCreatingAnimal_thenValidationErrors() {
        // Given
        String name = "Valid Name";
        int age = 52;

        // When
        Animal animal = new Pony(name, age);

        // Then
        Set<ConstraintViolation<Animal>> violations = validator.validate(animal);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Animal> violation = violations.iterator().next();
        Assertions.assertEquals("Age must be less than or equal to 50", violation.getMessage());
    }

    @Test
    public void givenAnimalWithoutStable_whenAddingStableToAnimal_thenAnimalHasStable() {
        // Given
        Animal animal = createDefaultPony();
        Stable stable = StableTest.createDefaultStable();

        // When
        animal.setStable(stable);

        // Then
        Assertions.assertEquals(stable, animal.getStable());
    }

    @Test
    public void givenAnimalWithStable_whenAddingStableToAnimal_thenDomainExceptionThrown() {
        // Given
        Animal animal = createDefaultPony();
        Stable stable = StableTest.createDefaultStable();
        animal.setStable(stable);
        Stable newStable = StableTest.createDefaultStable();

        // When
        Exception exception = Assertions.assertThrows(
            DomainException.class, () -> animal.setStable(newStable)
        );

        // Then
        String expectedMessage = "Animal " + animal.getName() + " is already assigned to a stable";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
    
    public static Pony createDefaultPony() {
        return new Pony(DEFAULT_NAME, DEFAULT_AGE);
    }


}
