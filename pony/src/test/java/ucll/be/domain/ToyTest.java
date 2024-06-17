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
import ucll.be.model.Toy;
import ucll.be.utilits.TimeTracker;

public class ToyTest {
    
    public static final String DEFAULT_NAME = "Default Name";
    
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
    public void givenValidToyData_whenCreatingToy_thenNoValidationErrors() {
        // Given
        String name = "Valid Name";

        // When
        Toy toy = new Toy(name);

        // Then
        Set<ConstraintViolation<Toy>> violations = validator.validate(toy);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void givenBlankName_whenCreatingToy_thenValidationErrors() {
        // Given
        String name = "";

        // When
        Toy toy = new Toy(name);

        // Then
        Set<ConstraintViolation<Toy>> violations = validator.validate(toy);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Toy> violation = violations.iterator().next();
        Assertions.assertEquals("Name is mandatory", violation.getMessage());
    }

    // GivenToyWithoutAnimals_whenAddingAnimal_thenAnimalIsAdded
    @Test
    public void givenToyWithoutAnimals_whenAddingAnimal_thenAnimalIsAdded() {
        // Given
        Toy toy = createDefaultToy();
        Animal animal = AnimalTest.createDefaultPony();

        // When
        toy.addAnimal(animal);

        // Then
        Assertions.assertTrue(toy.getAnimals().contains(animal));
    }
    // GivenToyWithAnimal_whenAddingTheSameAnimal_thenDomainExceptionThrown
    @Test
    public void givenToyWithAnimal_whenAddingTheSameAnimal_thenDomainExceptionThrown() {
        // Given
        Toy toy = createDefaultToy();
        Animal animal = AnimalTest.createDefaultPony();
        toy.addAnimal(animal);

        // When
        Exception exception = Assertions.assertThrows(
            DomainException.class, () -> toy.addAnimal(animal)
        );

        // Then
        String expectedMessage = "Animal \'" + animal.getName() +"\' is already added to this toy";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    public static Toy createDefaultToy() {
        return new Toy(DEFAULT_NAME);
    }
}
