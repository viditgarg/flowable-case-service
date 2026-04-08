package org.ny.its.flowable_case_service.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @NotEmpty(message = "First name is required.")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    private String firstName;
    @NotEmpty(message = "Last name is required.")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    private String lastName;

    private String email;
    @NotEmpty(message = "Please select a gender.")
    private String gender;

    @NotEmpty(message = "SSN is required.")
    @Pattern(regexp = "\\d{5}", message = "SSN must be exactly 5 digits.")
    private String ssn;

    @NotNull(message = "Date of birth is required.")
    private LocalDate dateofbirth;

    private String incarcerationStatus;
    private String updateIncarcerationStatus;
    private String ssnValid;
    private String updateSSNValid;
    private String ssnInWords;

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getSsn() {
        return ssn;
    }

    public LocalDate getDateofbirth() {
        return dateofbirth;
    }

    public String getIncarcerationStatus() {
        return incarcerationStatus;
    }

    public String getUpdateIncarcerationStatus() {
        return updateIncarcerationStatus;
    }

    public String getSsnValid() {
        return ssnValid;
    }

    public String getUpdateSSNValid() {
        return updateSSNValid;
    }

    public String getSsnInWords() {
        return ssnInWords;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public void setDateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public void setIncarcerationStatus(String incarcerationStatus) {
        this.incarcerationStatus = incarcerationStatus;
    }

    public void setUpdateIncarcerationStatus(String updateIncarcerationStatus) {
        this.updateIncarcerationStatus = updateIncarcerationStatus;
    }

    public void setSsnValid(String ssnValid) {
        this.ssnValid = ssnValid;
    }

    public void setUpdateSSNValid(String updateSSNValid) {
        this.updateSSNValid = updateSSNValid;
    }

    public void setSsnInWords(String ssnInWords) {
        this.ssnInWords = ssnInWords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) &&
                Objects.equals(lastName, person.lastName) &&
                Objects.equals(email, person.email) &&
                Objects.equals(gender, person.gender) &&
                Objects.equals(ssn, person.ssn) &&
                Objects.equals(dateofbirth, person.dateofbirth) &&
                Objects.equals(incarcerationStatus, person.incarcerationStatus) &&
                Objects.equals(updateIncarcerationStatus, person.updateIncarcerationStatus) &&
                Objects.equals(ssnValid, person.ssnValid) &&
                Objects.equals(updateSSNValid, person.updateSSNValid) &&
                Objects.equals(ssnInWords, person.ssnInWords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, gender, ssn, dateofbirth,
                incarcerationStatus, updateIncarcerationStatus, ssnValid, updateSSNValid, ssnInWords);
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", ssn='" + ssn + '\'' +
                ", dateofbirth=" + dateofbirth +
                ", incarcerationStatus='" + incarcerationStatus + '\'' +
                ", updateIncarcerationStatus='" + updateIncarcerationStatus + '\'' +
                ", ssnValid='" + ssnValid + '\'' +
                ", updateSSNValid='" + updateSSNValid + '\'' +
                ", ssnInWords='" + ssnInWords + '\'' +
                '}';
    }

    // Helper methods
    public List<String> getGenderOptions() {
        return List.of("Male", "Female", "Other", "Prefer not to say");
    }

    public List<String> getIncarcerationOptions() {
        return List.of("Select", "Incarcerated", "Not Incarcerated", "On Parole");
    }

    public List<String> getSsnValidationOptions() {
        return List.of("Select", "true", "false");
    }
}
