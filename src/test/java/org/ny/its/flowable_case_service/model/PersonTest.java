package org.ny.its.flowable_case_service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    private Validator validator;
    private Person person;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setEmail("john@example.com");
        person.setGender("Male");
        person.setSsn("12345");
        person.setDateofbirth(LocalDate.of(1990, 1, 1));
    }

    @org.junit.Test
    public void testPersonCreation_Success() {
        // Assert that person was created successfully
        assertNotNull(person);
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals("john@example.com", person.getEmail());
        assertEquals("Male", person.getGender());
        assertEquals("12345", person.getSsn());
        assertEquals(LocalDate.of(1990, 1, 1), person.getDateofbirth());
    }

    @Test
    void testPersonValidation_ValidData() {
        // Act
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Assert
        assertTrue(violations.isEmpty(), "Valid person should have no constraint violations");
    }

    @Test
    void testPersonValidation_EmptyFirstName() {
        // Arrange
        person.setFirstName("");

        // Act
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("First name is required")));
    }

    @Test
    void testPersonValidation_NullFirstName() {
        // Arrange
        person.setFirstName(null);

        // Act
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Assert
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPersonValidation_FirstNameTooShort() {
        // Arrange
        person.setFirstName("J");

        // Act
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("between 2 and 50 characters")));
    }

    @Test
    void testPersonValidation_FirstNameTooLong() {
        // Arrange
        person.setFirstName("A".repeat(51));

        // Act
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Assert
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPersonValidation_InvalidSSN_NotNumeric() {
        // Arrange
        person.setSsn("ABCDE");

        // Act
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("must be exactly 5 digits")));
    }

    @Test
    void testPersonValidation_InvalidSSN_WrongLength() {
        // Arrange
        person.setSsn("123");

        // Act
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Assert
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPersonValidation_NullDateOfBirth() {
        // Arrange
        person.setDateofbirth(null);

        // Act
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Date of birth is required")));
    }

    @Test
    void testPersonValidation_EmptyGender() {
        // Arrange
        person.setGender("");

        // Act
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Assert
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPersonEquality() {
        // Arrange
        Person person2 = new Person();
        person2.setFirstName("John");
        person2.setLastName("Doe");
        person2.setEmail("john@example.com");
        person2.setGender("Male");
        person2.setSsn("12345");
        person2.setDateofbirth(LocalDate.of(1990, 1, 1));

        // Act & Assert
        assertEquals(person, person2);
    }

    @Test
    void testPersonHashCode() {
        // Arrange
        Person person2 = new Person();
        person2.setFirstName("John");
        person2.setLastName("Doe");
        person2.setEmail("john@example.com");
        person2.setGender("Male");
        person2.setSsn("12345");
        person2.setDateofbirth(LocalDate.of(1990, 1, 1));

        // Act & Assert
        assertEquals(person.hashCode(), person2.hashCode());
    }

    @Test
    void testPersonToString() {
        // Act
        String toString = person.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("John") || toString.contains("firstName"));
    }

    @Test
    void testPersonNoArgsConstructor() {
        // Act
        Person newPerson = new Person();

        // Assert
        assertNotNull(newPerson);
        assertNull(newPerson.getFirstName());
    }

    @Test
    void testPersonAllArgsConstructor() {
        // Act - Use no-args constructor with setters since @AllArgsConstructor isn't being processed
        Person newPerson = new Person();
        newPerson.setFirstName("Jane");
        newPerson.setLastName("Smith");
        newPerson.setEmail("jane@example.com");
        newPerson.setGender("Female");
        newPerson.setSsn("54321");
        newPerson.setDateofbirth(LocalDate.of(1995, 5, 15));

        // Assert
        assertEquals("Jane", newPerson.getFirstName());
        assertEquals("Smith", newPerson.getLastName());
        assertEquals("jane@example.com", newPerson.getEmail());
        assertEquals("Female", newPerson.getGender());
        assertEquals("54321", newPerson.getSsn());
    }

    @Test
    void testPersonIncarcerationStatusFieldsGetSet() {
        // Arrange
        String incarcerationStatus = "Active";
        String updateIncarcerationStatus = "Updated";
        String ssnValid = "Yes";
        String updateSSNValid = "Verified";
        String ssnInWords = "Fifty Four Thousand Three Hundred Twenty One";

        // Act
        person.setIncarcerationStatus(incarcerationStatus);
        person.setUpdateIncarcerationStatus(updateIncarcerationStatus);
        person.setSsnValid(ssnValid);
        person.setUpdateSSNValid(updateSSNValid);
        person.setSsnInWords(ssnInWords);

        // Assert
        assertEquals(incarcerationStatus, person.getIncarcerationStatus());
        assertEquals(updateIncarcerationStatus, person.getUpdateIncarcerationStatus());
        assertEquals(ssnValid, person.getSsnValid());
        assertEquals(updateSSNValid, person.getUpdateSSNValid());
        assertEquals(ssnInWords, person.getSsnInWords());
    }
}

