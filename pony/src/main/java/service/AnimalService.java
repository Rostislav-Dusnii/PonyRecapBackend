package service;

import org.springframework.stereotype.Service;

import model.Animal;
import repository.AnimalRepository;

@Service
public class AnimalService {
    
    private AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal addAnimal(Animal entity) {
        animalRepository.save(entity);
        return animalRepository.findByName(entity.getName());
    }
}
