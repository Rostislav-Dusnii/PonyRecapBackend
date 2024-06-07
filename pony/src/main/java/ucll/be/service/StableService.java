package ucll.be.service;

import org.springframework.stereotype.Service;

import ucll.be.model.Animal;
import ucll.be.model.Stable;
import ucll.be.repository.StableRepository;

@Service
public class StableService {
    private StableRepository stableRepository;
    private AnimalService animalService;

    public StableService(StableRepository stableRepository, AnimalService animalService) {
        this.stableRepository = stableRepository;
        this.animalService = animalService;
    }

    public Stable assignAnimalToStable(String animalName, Stable stable) {
        Animal animal = animalService.getAnimalByName(animalName);
        animal.setStable(stable);
        stable.addAnimal(animal);

        stableRepository.save(stable);
        animalService.saveAnimal(animal);
        
        return stableRepository.findByName(stable.getName());
    }

    public Stable assignAnimalToStable(String animalName, Long stableId) {
        Stable stable = getStableById(stableId);
        return assignAnimalToStable(animalName, stable);
    }

    public Stable getStableById(Long id) {
        Stable stable = stableRepository.findById(id).orElse(null);
        if (stable == null) {
            throw new IllegalArgumentException("Stable with id " + id + " does not exist");
        }
        return stable;
    }
}