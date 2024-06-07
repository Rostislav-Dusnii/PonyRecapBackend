package ucll.be.model;


import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "MY_STABLES")
public class Stable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Positive
    private int maxAnimals;

    private List<Animal> animals;

    protected Stable() {
    }

    public Stable(String name, int maxAnimals) {
        setName(name);
        setMaxAnimals(maxAnimals);
    }

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

    public int getMaxAnimals() {
        return maxAnimals;
    }

    public void setMaxAnimals(int maxAnimals) {
        this.maxAnimals = maxAnimals;
    }

    public void addAnimal(Animal animal) {
        if (animals.size() >= maxAnimals) {
            throw new IllegalArgumentException("Stable with name " + name + " is full.");
        }
        animals.add(animal);
    }
}
