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
import ucll.be.utilits.TimeTracker;

public class AddressTest {
    public static final String DEFAULT_STREET = "Default Street";
    public static final int DEFAULT_NUMBER = 1;
    public static final String DEFAULT_PLACE = "Default City";

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
    public void givenValidAddressData_whenCreatingAddress_thenNoValidationErrors() {
        // Given
        String street = "Valid Street";
        int number = 123;
        String place = "Valid City";

        // When
        Address address = new Address(street, number, place);

        // Then
        Set<ConstraintViolation<Address>> violations = validator.validate(address);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void givenBlankStreet_whenCreatingAddress_thenValidationErrors() {
        // Given
        String street = "";
        int number = 123;
        String place = "Valid City";

        // When
        Address address = new Address(street, number, place);

        // Then
        Set<ConstraintViolation<Address>> violations = validator.validate(address);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Address> violation = violations.iterator().next();
        Assertions.assertEquals("Street cannot be blank", violation.getMessage());
    }

    @Test
    public void givenNegativeNumber_whenCreatingAddress_thenValidationErrors() {
        // Given
        String street = "Valid Street";
        int number = -123;
        String place = "Valid City";

        // When
        Address address = new Address(street, number, place);

        // Then
        Set<ConstraintViolation<Address>> violations = validator.validate(address);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Address> violation = violations.iterator().next();
        Assertions.assertEquals("Number must be a positive number", violation.getMessage());
    }
    @Test
    public void givenBlankPlace_whenCreatingAddress_thenValidationErrors() {
        // Given
        String street = "Valid Street";
        int number = 123;
        String place = "";

        // When
        Address address = new Address(street, number, place);

        // Then
        Set<ConstraintViolation<Address>> violations = validator.validate(address);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Address> violation = violations.iterator().next();
        Assertions.assertEquals("Place cannot be blank", violation.getMessage());
    }

    public static Address createDefaultAddress() {
        return new Address(DEFAULT_STREET, DEFAULT_NUMBER, DEFAULT_PLACE);
    }
}
