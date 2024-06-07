package ucll.be.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import ucll.be.model.Animal;

@Component
public class DbInitializer {
    private AnimalRepository animalRepository;

    public DbInitializer(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @PostConstruct
    public void populateDB() {
        List<Animal> animals = createAnimals();
        for (Animal animal : animals) {
            animalRepository.save(animal);
        }

    }

    public List<Animal> createAnimals() {
        List<Animal> animals = new ArrayList<>();
        Animal animal1 = new Animal("Bella", 20);
        Animal animal2 = new Animal("Luna", 10);
        Animal animal3 = new Animal("Muriel", 2);
        Animal animal4 = new Animal("Little", 1);
        animals.add(animal1);
        animals.add(animal2);
        animals.add(animal3);
        animals.add(animal4);
        return animals;
    }
}
