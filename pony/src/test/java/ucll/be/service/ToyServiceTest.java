package ucll.be.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ucll.be.domain.AnimalTest;
import ucll.be.domain.ToyTest;
import ucll.be.model.Animal;
import ucll.be.model.Toy;
import ucll.be.utilits.TimeTracker;
import ucll.be.utils.AnimalRepositoryTestImpl;
import ucll.be.utils.ToyRepositoryTestImpl;

public class ToyServiceTest {

    private ToyService toyService;
    private ToyRepositoryTestImpl toyRepository;
    private AnimalService animalService;
    private AnimalRepositoryTestImpl animalRepository;

    
    @BeforeEach
    public void resetTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
        toyRepository = new ToyRepositoryTestImpl(); // Replace with your actual ToyRepository implementation
        animalRepository = AnimalServiceTest.createDefaultAnimalRepository();
        animalService = new AnimalService(animalRepository);
        toyService = new ToyService(toyRepository, animalService);
    }

    @Test
    public void givenToyId_whenGetToyById_thenReturnToy() {
        // Arrange
        toyRepository.toys = new ArrayList<>();
        Toy toy = ToyTest.createDefaultToy();
        toy.setId(1L);
        toyRepository.save(toy);

        // Act
        Toy retrievedToy = toyService.getToyById(1L);

        // Assert
        assertEquals(toy, retrievedToy);
    }

    @Test
    public void givenToyName_whenGetToyByName_thenReturnToy() {
        // Arrange
        Toy toy = ToyTest.createDefaultToy();
        toy.setName("Test Toy");
        toyRepository.save(toy);

        // Act
        Toy retrievedToy = toyService.getToyByName("Test Toy");

        // Assert
        assertEquals(toy, retrievedToy);
    }

    @Test
    public void givenNewToy_whenAddToy_thenToyAdded() {
        // Arrange
        Toy toy = ToyTest.createDefaultToy();
        toy.setName("New Toy");

        // Act
        Toy addedToy = toyService.addToy(toy);

        // Assert
        assertEquals(toy, addedToy);
        assertTrue(toyRepository.findAll().contains(toy));
    }

    @Test
    public void givenToyAndAnimalIds_whenAddAnimalToToy_thenAnimalAddedToToy() {
        // Arrange
        toyRepository.toys = new ArrayList<>();
        animalRepository.animals = new ArrayList<>();
        Toy toy = ToyTest.createDefaultToy();
        toy.setId(1L);
        toyRepository.save(toy);
        

        Animal animal = AnimalTest.createDefaultAnimal();
        animal.setId(1L);
        animalRepository.save(animal);

        // Act
        Toy updatedToy = toyService.addAnimalToToy(1L, 1L);

        // Assert
        assertTrue(updatedToy.getAnimals().contains(animal));
    }
}
