package ucll.be.service;

import org.springframework.stereotype.Service;

import ucll.be.model.Animal;
import ucll.be.model.Toy;
import ucll.be.repository.ToyRepository;

@Service
public class ToyService {
    
    private ToyRepository toyRepository;
    private AnimalService animalService;

    public ToyService(ToyRepository toyRepository, AnimalService animalService) {
        this.toyRepository = toyRepository;
        this.animalService = animalService;
    }

    public Toy getToyById(Long id) {
        return toyRepository.findById(id).orElseThrow(() -> new ServiceException("Toy with id " + id + " does not exist"));
    }

    public Toy getToyByName(String name) {
        return toyRepository.findByNameIgnoreCase(name).orElseThrow(() -> new ServiceException("Toy with name " + name + " does not exist"));
    }

    public Toy addToy(Toy toy) {
        throwErrorIfExists(toy);
        return toyRepository.save(toy);
    }

    public Toy addAnimalToToy(Long toyId, Long animalId) {
        Toy toy = getToyById(toyId);
        Animal animal = animalService.getAnimalById(animalId);
        
        toy.addAnimal(animal);
        return toyRepository.save(toy);
    }

    public void throwErrorIfExists(Toy toy) {
        if (toyRepository.existsByNameIgnoreCase(toy.getName())) {
            throw new ServiceException("Toy already exists");
        }
    }   
}
