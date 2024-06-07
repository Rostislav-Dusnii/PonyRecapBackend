package ucll.be.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import ucll.be.model.Animal;
import ucll.be.model.Stable;

@Component
public class DbInitializer {
    private AnimalRepository animalRepository;
    private StableRepository stableRepository;

    public DbInitializer(AnimalRepository animalRepository, StableRepository stableRepository) {
        this.animalRepository = animalRepository;
        this.stableRepository = stableRepository;
    }

    @PostConstruct
    public void populateDB() {
        List<Animal> animals = createAnimals();
        List<Stable> stables = createStables();
        connectStablesAndAnimals(animals, stables);

        stableRepository.saveAll(stables);
        animalRepository.saveAll(animals);
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

    public List<Stable> createStables() {
        List<Stable> stables = new ArrayList<>();
        Stable stable1 = new Stable("StblHn", 5);
        Stable stable2 = new Stable("PonyCo", 3);
        stables.add(stable1);
        stables.add(stable2);
        return stables;
    }

    public void connectStablesAndAnimals(List<Animal> animals, List<Stable> stables) {
        // Animal Luna is assigned to a new stable “StblHn” with place for 5 animals
        // Animal Muriel is assigned to a new stable “PonyCo” with place for 3 animals
        String murielName = "Muriel";
        String lunaName = "Luna";
        String stblHnName = "StblHn";
        String ponyCoName = "PonyCo";
        Animal muriel = animals.stream().filter(animal -> animal.getName().equals(murielName)).findFirst().orElse(null);
        Animal luna = animals.stream().filter(animal -> animal.getName().equals(lunaName)).findFirst().orElse(null);
        Stable stblHn = stables.stream().filter(stable -> stable.getName().equals(stblHnName)).findFirst().orElse(null);
        Stable ponyCo = stables.stream().filter(stable -> stable.getName().equals(ponyCoName)).findFirst().orElse(null);
        
        luna.setStable(stblHn);
        stblHn.addAnimal(luna);
        muriel.setStable(ponyCo);
        ponyCo.addAnimal(muriel);
    }
}
