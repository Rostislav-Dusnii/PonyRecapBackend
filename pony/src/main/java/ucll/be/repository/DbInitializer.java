package ucll.be.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import ucll.be.model.Address;
import ucll.be.model.Animal;
import ucll.be.model.Chicken;
import ucll.be.model.MedicalRecord;
import ucll.be.model.Pony;
import ucll.be.model.Stable;
import ucll.be.model.Toy;
import ucll.be.utilits.TimeTracker;

@Component
public class DbInitializer {
    private AnimalRepository animalRepository;
    private StableRepository stableRepository;
    private AddressRepository addressRepository;
    private ToyRepository toyRepository;
    private MedicalRecordsRepository medicalRecordsRepository;

    public DbInitializer(AnimalRepository animalRepository, StableRepository stableRepository, AddressRepository addressRepository, ToyRepository toyRepository, MedicalRecordsRepository medicalRecordsRepository) {
        this.animalRepository = animalRepository;
        this.stableRepository = stableRepository;
        this.addressRepository = addressRepository;
        this.toyRepository = toyRepository;
        this.medicalRecordsRepository = medicalRecordsRepository;
    }

    @PostConstruct
    public void populateDB() {
        List<Animal> animals = createAnimals();
        List<Address> addresses = createAddresses();
        List<Stable> stables = createStables(addresses);
        List<MedicalRecord> medicalRecords = createMedicalRecords(animals);
        List<Toy> toys = createToys();
        connectStablesAndAnimals(animals, stables);
        connectToysAndAnimals(toys, animals);

        addresses = addressRepository.saveAll(addresses);
        connectStablesAndAddresses(addresses, stables);
        stableRepository.saveAll(stables);
        animalRepository.saveAll(animals);
        medicalRecordsRepository.saveAll(medicalRecords);
        toyRepository.saveAll(toys);
    }

    public static List<Animal> createAnimals() {
        List<Animal> animals = new ArrayList<>();
        Animal animal1 = new Pony("Bella", 20);
        Animal animal2 = new Pony("Luna", 10);

        animals.add(animal1);
        animals.add(animal2);

        Animal animal3 = new Chicken("Muriel", true);
        Animal animal4 = new Chicken("Little", false);
        animals.add(animal3);
        animals.add(animal4);
        return animals;
    }

    public static List<Stable> createStables(List<Address> addresses) {
        List<Stable> stables = new ArrayList<>();
        Stable stable1 = new Stable("StblHn", 5);
        Stable stable2 = new Stable("PonyCo", 3);

        stables.add(stable1);
        stables.add(stable2);
        return stables;
    }

    public static List<Address> createAddresses() {
        List<Address> addresses = new ArrayList<>();
        Address address1 = new Address("Horsestreet", 1, "Leuven");
        Address address2 = new Address("Ponyroad", 2, "Leuven");
        addresses.add(address1);
        addresses.add(address2);
        return addresses;
    }

    public static List<Toy> createToys() {
        List<Toy> toys = new ArrayList<>();
        Toy toy1 = new Toy("Ball");
        Toy toy2 = new Toy("Rope");
        Toy toy3 = new Toy("Carrot");
        toys.add(toy1);
        toys.add(toy2);
        toys.add(toy3);
        return toys;
    }

    public static List<MedicalRecord> createMedicalRecords(List<Animal> animals) {
        Animal animalBella = animals.stream().filter(animal -> animal.getName().equals("Bella")).findFirst().orElse(null);
        Animal animalLuna = animals.stream().filter(animal -> animal.getName().equals("Luna")).findFirst().orElse(null);

        LocalDate today = TimeTracker.getToday();

        List<MedicalRecord> medicalRecords = new ArrayList<>();
        MedicalRecord medicalRecord1 = new MedicalRecord(today.minusDays(1), "Bella has a cold", animalBella);
        MedicalRecord medicalRecord2 = new MedicalRecord(today.minusDays(2), "Luna has a broken leg", animalLuna);
        medicalRecords.add(medicalRecord1);
        medicalRecords.add(medicalRecord2);
        
        return medicalRecords;
    }

    public static void connectStablesAndAddresses(List<Address> addresses, List<Stable> stables) {
        Address horseStreet = addresses.stream().filter(address -> address.getStreet().equals("Horsestreet")).findFirst().orElse(null);
        Address ponyRoad = addresses.stream().filter(address -> address.getStreet().equals("Ponyroad")).findFirst().orElse(null);
        Stable stable1 = stables.stream().filter(stable -> stable.getName().equals("StblHn")).findFirst().orElse(null);
        Stable stable2 = stables.stream().filter(stable -> stable.getName().equals("PonyCo")).findFirst().orElse(null);
        
        // horseStreet.setStable(stable1);
        // ponyRoad.setStable(stable2);
        stable1.setAddress(horseStreet);
        stable2.setAddress(ponyRoad);
    }

    public static void connectStablesAndAnimals(List<Animal> animals, List<Stable> stables) {
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

    public static void connectToysAndAnimals(List<Toy> toys, List<Animal> animals) {
        // Animal Bella has a ball and a rope
        // Animal Little has a carrot
        String bellaName = "Bella";
        String littleName = "Little";
        String ballName = "Ball";
        String ropeName = "Rope";
        String carrotName = "Carrot";
        Animal bella = animals.stream().filter(animal -> animal.getName().equals(bellaName)).findFirst().orElse(null);
        Animal little = animals.stream().filter(animal -> animal.getName().equals(littleName)).findFirst().orElse(null);
        Toy ball = toys.stream().filter(toy -> toy.getName().equals(ballName)).findFirst().orElse(null);
        Toy rope = toys.stream().filter(toy -> toy.getName().equals(ropeName)).findFirst().orElse(null);
        Toy carrot = toys.stream().filter(toy -> toy.getName().equals(carrotName)).findFirst().orElse(null);
        
        ball.addAnimal(bella);
        rope.addAnimal(bella);
        carrot.addAnimal(little);
    }
}
