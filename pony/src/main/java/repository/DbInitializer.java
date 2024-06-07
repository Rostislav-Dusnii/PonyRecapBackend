package repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import model.Animal;

@Component
public class DbInitializer {
    private AnimalRepository animalRepository;

    public DbInitializer(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @PostConstruct
    public void populateDB() {
        List<Animal> animals = createAnimals();
        animalRepository.saveAll(animals);
    }

    public List<Animal> createAnimals() {
        Animal animal1 = new Animal("Bella", 20);
        Animal animal2 = new Animal("Luna", 10);
        Animal animal3 = new Animal("Muriel", 2);
        Animal animal4 = new Animal("Little", 1);
        List<Animal> animals = new ArrayList<>(List.of(animal1, animal2, animal3, animal4));
        return animals;
    }
}
