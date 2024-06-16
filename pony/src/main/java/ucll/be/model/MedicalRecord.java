package ucll.be.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ucll.be.utilits.TimeTracker;

@Entity
@Table(name = "MEDICAL_RECORDS")
public class MedicalRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate registrationDate;
    
    private LocalDate closingDate;
    
    @NotBlank(message = "Description is required")
    private String description;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    @NotNull(message = "Animal is required")
    private Animal animal;

    public MedicalRecord() {
    }

    public MedicalRecord(LocalDate registrationDate, String description, Animal animal) {
        setRegistrationDate(registrationDate);
        setDescription(description);
        setAnimal(animal);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        if (registrationDate.isAfter(TimeTracker.getToday())) {
            throw new DomainException("Registration date cannot be in the future");
        }
        this.registrationDate = registrationDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        if (closingDate != null && closingDate.isBefore(registrationDate.plusDays(1))) {
            throw new DomainException("Closing date must be at least 1 day after registration date");
        }
        this.closingDate = closingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}
