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
        Animal animal = animalRepository.findByNameIgnoreCase(name);
        if (animal == null) {
            throw new IllegalArgumentException("Animal with name " + name + " does not exist");
        }

        return animal;
    }

    public Animal getAnimalById(Long id) {
        Animal animal = animalRepository.findById(id).orElse(null);
        if (animal == null) {
            throw new IllegalArgumentException("Animal with id " + id + " does not exist");
        }

        return animal;
    }

    public List<Animal> getAnimalsWithOpenMedicalRecords() {
        return animalRepository.findAnimalsWithOpenMedicalRecords();
    }

    public Animal addAnimal(Animal entity) {
        String name = entity.getName();
        throwErrorIfExists(name);
        animalRepository.save(entity);
        return animalRepository.findByNameIgnoreCase(name);
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

    public void saveAnimal(Animal animal) {
        animalRepository.save(animal);
    }   

    public void throwErrorIfExists(String name) {
        if (getAnimalByName(name) != null) {
            throw new IllegalArgumentException("Animal with name " + name + " already exists");
        }
    }
}
