package ucll.be.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ucll.be.model.Animal;
import ucll.be.repository.AnimalRepository;

@Service
public class AnimalService {
    
    private AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal getAnimalByName(String name) {
        return animalRepository.findByName(name);
    }

    public Animal addAnimal(Animal entity) {
        String name = entity.getName();
        throwErrorIfExists(name);
        animalRepository.save(entity);
        return animalRepository.findByName(name);
    }

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public List<Animal> getAnimalsOlderThan(int age) {
        return animalRepository.findByAgeGreaterThan(age);
    }

    public Animal getOldestAnimal() {
        return animalRepository.findFirstByOrderByAgeDesc();
    }

    public void throwErrorIfExists(String name) {
        if (getAnimalByName(name) != null) {
            throw new IllegalArgumentException("Animal with name " + name + " already exists");
        }
    }
}