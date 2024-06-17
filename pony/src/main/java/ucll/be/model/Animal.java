package ucll.be.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "MY_ANIMALS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "animal_type")
public abstract class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @ManyToOne
    @JoinColumn(name = "stable_id")
    @JsonBackReference
    private Stable stable;

    @Column(name = "animal_type", insertable = false, updatable = false)
    private String animalType;

    protected Animal() {
        // Empty constructor for JPA
    }

    public Animal(String name) {
        setName(name);
    }

    // Setters and Getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Stable getStable() {
        return stable;
    }

    public void setStable(Stable stable) {
        if (this.stable != null) {
            throw new DomainException("Animal " + name + " is already assigned to a stable");
        }
        this.stable = stable;
    }
}