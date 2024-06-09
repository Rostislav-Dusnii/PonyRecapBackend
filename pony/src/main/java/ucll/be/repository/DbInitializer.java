package ucll.be.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import ucll.be.model.Address;
import ucll.be.model.Animal;
import ucll.be.model.Stable;

@Component
public class DbInitializer {
    private AnimalRepository animalRepository;
    private StableRepository stableRepository;
    private AddressRepository addressRepository;

    public DbInitializer(AnimalRepository animalRepository, StableRepository stableRepository, AddressRepository addressRepository) {
        this.animalRepository = animalRepository;
        this.stableRepository = stableRepository;
        this.addressRepository = addressRepository;
    }

    @PostConstruct
    public void populateDB() {
        List<Animal> animals = createAnimals();
        List<Address> addresses = createAddresses();
        List<Stable> stables = createStables(addresses);
        connectStablesAndAnimals(animals, stables);

        addresses = addressRepository.saveAll(addresses);
        connectStablesAndAddresses(addresses, stables);
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

    public List<Stable> createStables(List<Address> addresses) {
        List<Stable> stables = new ArrayList<>();
        Stable stable1 = new Stable("StblHn", 5);
        Stable stable2 = new Stable("PonyCo", 3);

        stables.add(stable1);
        stables.add(stable2);
        return stables;
    }

    public List<Address> createAddresses() {
        List<Address> addresses = new ArrayList<>();
        Address address1 = new Address("Horsestreet", 1, "Leuven");
        Address address2 = new Address("Ponyroad", 2, "Leuven");
        addresses.add(address1);
        addresses.add(address2);
        return addresses;
    }

    public void connectStablesAndAddresses(List<Address> addresses, List<Stable> stables) {
        Address horseStreet = addresses.stream().filter(address -> address.getStreet().equals("Horsestreet")).findFirst().orElse(null);
        Address ponyRoad = addresses.stream().filter(address -> address.getStreet().equals("Ponyroad")).findFirst().orElse(null);
        Stable stable1 = stables.stream().filter(stable -> stable.getName().equals("StblHn")).findFirst().orElse(null);
        Stable stable2 = stables.stream().filter(stable -> stable.getName().equals("PonyCo")).findFirst().orElse(null);
        
        // horseStreet.setStable(stable1);
        // ponyRoad.setStable(stable2);
        stable1.setAddress(horseStreet);
        stable2.setAddress(ponyRoad);
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
