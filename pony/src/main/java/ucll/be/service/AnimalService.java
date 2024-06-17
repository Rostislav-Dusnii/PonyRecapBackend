package ucll.be.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ucll.be.model.Animal;
import ucll.be.model.Chicken;
import ucll.be.model.Pony;
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
            throw new ServiceException("Animal with name " + name + " does not exist");
        }

        return animal;
    }

    public Animal getAnimalById(Long id) {
        Animal animal = animalRepository.findById(id).orElse(null);
        if (animal == null) {
            throw new ServiceException("Animal with id " + id + " does not exist");
        }

        return animal;
    }

    public List<Animal> getAnimalsWithOpenMedicalRecords() {
        return animalRepository.findAnimalsWithOpenMedicalRecords();
    }

    public Animal addAnimal(Animal entity) {
        String name = entity.getName();
        throwErrorIfExists(name);
        return animalRepository.save(entity);
    }

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public List<Pony> getPonies() {
        return animalRepository.findAllPonies();
    }

    public List<Chicken> getChickens(Boolean onlyLaysEggs) {
        if (onlyLaysEggs == null || !onlyLaysEggs) {
            return animalRepository.findAllChickens();
        }
        return animalRepository.findAllChickensThatLayEggs();
    }

    public List<Pony> getAnimalsOlderThan(int age) {
        return animalRepository.findByAgeGreaterThan(age);
    }

    public Animal getOldestAnimal() {
        return animalRepository.findFirstByOrderByAgeDesc();
    }

    public void saveAnimal(Animal animal) {
        animalRepository.save(animal);
    }   

    public void throwErrorIfExists(String name) {
        if (animalRepository.findByNameIgnoreCase(name) != null) {
            throw new ServiceException("Animal with name " + name + " already exists");
        }
    }

    public void throwErrorIfNotExists(Animal animal) {
        Long id = animal.getId();

        if (!animalRepository.existsById(id)) {
            throw new ServiceException("Animal with id " + id + " does not exist");
        }
    }
}
