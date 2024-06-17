package ucll.be.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ucll.be.domain.AnimalTest;
import ucll.be.model.Animal;
import ucll.be.model.Pony;
import ucll.be.utilits.TimeTracker;
import ucll.be.utils.AnimalRepositoryTestImpl;

public class AnimalServiceTest {

    private AnimalService animalService;
    private AnimalRepositoryTestImpl animalRepository;

    
    @BeforeEach
    public void resetTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
        animalRepository = createDefaultAnimalRepository();
        animalService = new AnimalService(animalRepository);
    }

    @Test
    public void givenAnimal_whenAddAnimal_thenAnimalAdded() {
        Animal animal = AnimalTest.createDefaultPony();
        int expectedSize = animalRepository.findAll().size() + 1;

        animalService.addAnimal(animal);

        assertEquals(expectedSize, animalRepository.findAll().size());
        assertTrue(animalRepository.findAll().contains(animal));
    }  

    @Test
    public void givenAnimalName_whenGetAnimalByName_thenAnimalReturned() {

        Animal animal = AnimalTest.createDefaultPony();
        animalRepository.save(animal);

        Animal retrievedAnimal = animalService.getAnimalByName(animal.getName());

        assertEquals(animal, retrievedAnimal);
    }

    @Test
    public void givenAnimalId_whenGetAnimalById_thenAnimalReturned() {

        Animal animal = AnimalTest.createDefaultPony();
        animal.setId(Long.valueOf(5));
        animalRepository.save(animal);

        Animal retrievedAnimal = animalService.getAnimalById(animal.getId());

        assertEquals(animal, retrievedAnimal);
    }

    @Test
    public void givenAnimalsWithOpenMedicalRecords_whenGetAnimalsWithOpenMedicalRecords_thenAnimalsReturned() {

        Animal animal1 = AnimalTest.createDefaultPony();
        animal1.setId(Long.valueOf(7));
        animalRepository.medicalRecords.get(0).setAnimal(animal1);
        animalRepository.save(animal1);

        Animal animal2 = AnimalTest.createDefaultPony();
        animal2.setId(Long.valueOf(6));
        animalRepository.medicalRecords.get(1).setAnimal(animal2);
        animalRepository.save(animal2);

        List<Animal> animalsWithOpenMedicalRecords = animalService.getAnimalsWithOpenMedicalRecords();

        assertEquals(2, animalsWithOpenMedicalRecords.size());
        assertTrue(animalsWithOpenMedicalRecords.contains(animal1));
    }

    @Test
    public void givenAnimals_whenGetAllAnimals_thenEmptyListReturned() {

        List<Animal> allAnimals = animalService.getAllAnimals();

        assertTrue(allAnimals.size() == animalRepository.findAll().size());
    }

    @Test
    public void givenAnimalsWithDifferentAges_whenGetAnimalsOlderThan_thenAnimalsReturned() {
        animalRepository.animals = new ArrayList<>();

        Pony animal1 = AnimalTest.createDefaultPony();
        animal1.setAge(5);
        animalRepository.save(animal1);

        Pony animal2 = AnimalTest.createDefaultPony();
        animal2.setAge(3);
        animalRepository.save(animal2);

        List<Pony> animalsOlderThanThree = animalService.getAnimalsOlderThan(3);

        assertEquals(1, animalsOlderThanThree.size());
        assertTrue(animalsOlderThanThree.contains(animal1));
    }

    @Test
    public void givenAnimalsWithDifferentAges_whenGetOldestAnimal_thenOldestAnimalReturned() {
        animalRepository.animals = new ArrayList<>();

        Pony animal1 = AnimalTest.createDefaultPony();
        animal1.setAge(5);
        animalRepository.save(animal1);

        Pony animal2 = AnimalTest.createDefaultPony();
        animal2.setAge(3);
        animalRepository.save(animal2);

        Animal oldestAnimal = animalService.getOldestAnimal();

        assertEquals(animal1, oldestAnimal);
    }

    @Test
    public void givenAnimal_whenSaveAnimal_thenAnimalSaved() {

        Animal animal = AnimalTest.createDefaultPony();
        animalService.saveAnimal(animal);

        assertTrue(animalRepository.findAll().contains(animal));
    }

    @Test
    public void givenExistingAnimalName_whenThrowErrorIfExists_thenServiceExceptionThrown() {

        Animal animal = AnimalTest.createDefaultPony();
        animalRepository.save(animal);

        assertThrows(ServiceException.class, () -> animalService.throwErrorIfExists(animal.getName()));
    }

    @Test
    public void givenNonExistingAnimal_whenThrowErrorIfNotExists_thenServiceExceptionThrown() {

        Animal animal = AnimalTest.createDefaultPony();

        assertThrows(ServiceException.class, () -> animalService.throwErrorIfNotExists(animal));
    }

    public static AnimalRepositoryTestImpl createDefaultAnimalRepository() {
        return new AnimalRepositoryTestImpl();
    }

    public static AnimalService createDefaultAnimalService() {
        return new AnimalService(createDefaultAnimalRepository());
    }
}
