package ucll.be.service;

import java.util.List;

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

    public Stable getStableByAnimalName(String animalName) {
        Animal animal = animalService.getAnimalByName(animalName);
        Stable animStable = animal.getStable();
        if (animStable == null) {
            throw new IllegalArgumentException("Animal with name " + animalName + " is not assigned to a stable");
        }
        return animStable;
    }

    public List<Stable> getAllStables() {
        return stableRepository.findAll();
    }

    public Stable getStableById(Long id) {
        Stable stable = stableRepository.findById(id).orElse(null);
        if (stable == null) {
            throw new IllegalArgumentException("Stable with id " + id + " does not exist");
        }
        return stable;
    }
}