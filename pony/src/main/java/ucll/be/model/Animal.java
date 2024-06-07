package ucll.be.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "MY_ANIMALS")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 1, message = "Age must be greater than or equal to 1")
    @Max(value = 50, message = "Age must be less than or equal to 50")
    private int age;

    @ManyToOne
    @JoinColumn(name = "id")
    private Stable stable;

    protected Animal() {
        // Empty constructor for JPA
    }

    public Animal(String name, int age) {
        setName(name);
        setAge(age);
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Stable getStable() {
        return stable;
    }

    public void setStable(Stable stable) {
        if (this.stable != null) {
            throw new IllegalArgumentException("Animal " + name + " is already assigned to a stable");
        }
        this.stable = stable;
    }
}