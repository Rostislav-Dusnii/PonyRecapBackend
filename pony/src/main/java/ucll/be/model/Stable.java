package ucll.be.model;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "MY_STABLES")
public class Stable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Positive(message = "Max animals must be positive")
    private int maxAnimals;

    @OneToMany(mappedBy = "stable", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Animal> animals;

    private Long addressId;

    @Transient
    @JsonIgnore
    private Address address;

    protected Stable() {
    }

    public Stable(String name, int maxAnimals) {
        setName(name);
        setMaxAnimals(maxAnimals);
        animals = new ArrayList<>();
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
    
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (this.address != null) {
            throw new DomainException("Address is already set for " + name + " stable");
        }
        this.address = address;
        setAddressId(address.getId());
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public void addAnimal(Animal animal) {
        if (animals.contains(animal)) {
            throw new DomainException("Animal " + animal.getName() + " is already in stable " + name);
        }
        if (animals.size() >= maxAnimals) {
            throw new DomainException("Stable with name " + name + " is full.");
        }
        animals.add(animal);
    }
}
