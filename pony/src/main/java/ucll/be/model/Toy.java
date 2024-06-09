package ucll.be.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "TOYS")
public class Toy {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @ManyToMany
    @JoinTable(
        name = "ANIMAL_TOYS",
        joinColumns = @JoinColumn(name = "toy_id"),
        inverseJoinColumns = @JoinColumn(name = "animal_id")
    )
    private List<Animal> animals;

    protected Toy() {
        // Empty constructor for JPA
    }

    public Toy(String name) {
        setName(name);
        animals = new ArrayList<>();
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

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void addAnimal(Animal animal) {
        if (animals.contains(animal)) {
            throw new DomainException("Animal \'" + animal.getName() +"\' is already added to this toy");
        }
        animals.add(animal);
    }
}
