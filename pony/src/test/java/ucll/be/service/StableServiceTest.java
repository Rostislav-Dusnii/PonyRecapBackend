package ucll.be.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ucll.be.domain.AddressTest;
import ucll.be.domain.AnimalTest;
import ucll.be.domain.StableTest;
import ucll.be.model.Address;
import ucll.be.model.Animal;
import ucll.be.model.Stable;
import ucll.be.repository.AddressRepository;
import ucll.be.repository.StableRepository;
import ucll.be.utilits.TimeTracker;

@ExtendWith(MockitoExtension.class)
public class StableServiceTest {
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private StableRepository stableRepository;
    @Mock 
    private AnimalService animalService;
    @InjectMocks
    private StableService stableService;
    @Mock
    private AddressService addressService;
    
    @BeforeEach
    public void resetTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
    }

    //     public Stable addStable(Stable entity) {
    //     String name = entity.getName();
    //     throwErrorIfExists(name);

    //     Long addressId = entity.getAddressId();
    //     if (addressId != null) {
    //         Address address = addressService.getAddressById(addressId);
    //         entity.setAddress(address);
    //     }
    //     return stableRepository.save(entity);
    // }

    // public Stable assignStableToAddress(Long stableId, Long addressId) {
    //     Stable stable = getStableById(stableId);
    //     Address address = addressService.getAddressById(addressId);
        
    //     stable.setAddress(address);

    //     return stableRepository.save(stable);
    // }

    // public Stable assignAnimalToStable(String animalName, Stable stable) {
    //     Animal animal = animalService.getAnimalByName(animalName);
    //     animal.setStable(stable);
    //     stable.addAnimal(animal);

    //     stableRepository.save(stable);
    //     animalService.saveAnimal(animal);
        
    //     return stableRepository.findByName(stable.getName());
    // }

    // public Stable assignAnimalToStable(String animalName, Long stableId) {
    //     Stable stable = getStableById(stableId);
    //     return assignAnimalToStable(animalName, stable);
    // }

    // public Stable getStableByAnimalName(String animalName) {
    //     Animal animal = animalService.getAnimalByName(animalName);
    //     Stable animStable = animal.getStable();
    //     if (animStable == null) {
    //         throw new ServiceException("Animal with name " + animalName + " is not assigned to a stable");
    //     }
    //     return animStable;
    // }

    // public List<Stable> getAllStables() {
    //     return stableRepository.findAll();
    // }

    // public Stable getStableById(Long id) {
    //     Stable stable = stableRepository.findById(id).orElse(null);
    //     if (stable == null) {
    //         throw new ServiceException("Stable with id " + id + " does not exist");
    //     }
    //     return stable;
    // }

    @Test
    public void givenStableEntity_whenAddingStable_thenStableSaved() {
        // Given
        Stable entity = StableTest.createDefaultStable();

        Long addressId = 1L;
        entity.setAddressId(addressId);

        Address expectedAddress = AddressTest.createDefaultAddress();
        expectedAddress.setId(addressId);
        when(addressService.getAddressById(addressId)).thenReturn(expectedAddress);

        Stable expectedStable = StableTest.createDefaultStable();
        when(stableRepository.save(entity)).thenReturn(expectedStable);

        // When
        Stable actualStable = stableService.addStable(entity);

        // Then
        assertEquals(expectedStable, actualStable);
        verify(addressService).getAddressById(addressId);
        verify(stableRepository).save(entity);
    }

    @Test
    public void givenStableIdAndAddressId_whenAssigningStableToAddress_thenStableWithUpdatedAddressReturned() {
        // Given
        Long stableId = 1L;
        Long addressId = 2L;

        Stable expectedStable = StableTest.createDefaultStable();

        Address expectedAddress = AddressTest.createDefaultAddress();
        when(addressService.getAddressById(addressId)).thenReturn(expectedAddress);

        Stable expectedUpdatedStable = StableTest.createDefaultStable();
        when(stableRepository.save(expectedStable)).thenReturn(expectedUpdatedStable);
        when(stableRepository.findById(stableId)).thenReturn(Optional.of(expectedStable));

        // When
        Stable actualStable = stableService.assignStableToAddress(stableId, addressId);

        // Then
        assertEquals(expectedUpdatedStable, actualStable);
        verify(stableRepository).findById(stableId);
        verify(addressService).getAddressById(addressId);
        verify(stableRepository).save(expectedStable);
    }

    @Test
    public void givenAnimalNameAndStable_whenAssigningAnimalToStable_thenAnimalAssignedAndStableUpdated() {
        // Given
        String animalName = AnimalTest.DEFAULT_NAME;
        Stable stable = StableTest.createDefaultStable();

        Animal expectedAnimal = AnimalTest.createDefaultPony();
        when(animalService.getAnimalByName(animalName)).thenReturn(expectedAnimal);

        Stable expectedUpdatedStable = StableTest.createDefaultStable();
        when(stableRepository.findByName(stable.getName())).thenReturn(expectedUpdatedStable);

        // When
        Stable actualStable = stableService.assignAnimalToStable(animalName, stable);

        // Then
        assertEquals(expectedUpdatedStable, actualStable);
        verify(animalService).getAnimalByName(animalName);
        verify(stableRepository).save(stable);
        verify(animalService).saveAnimal(expectedAnimal);
    }

    @Test
    public void givenAnimalNameAndStableId_whenAssigningAnimalToStable_thenAnimalAssignedAndStableUpdated() {
        // Given
        String animalName = AnimalTest.DEFAULT_NAME;
        Long stableId = 1L;
        Animal expectedAnimal = AnimalTest.createDefaultPony();
        when(animalService.getAnimalByName(animalName)).thenReturn(expectedAnimal);

        Stable expectedStable = StableTest.createDefaultStable();
        when(stableRepository.findById(stableId)).thenReturn(Optional.of(expectedStable));

        doReturn(expectedStable).when(stableRepository).findByName(expectedStable.getName());

        // When
        Stable actualStable = stableService.assignAnimalToStable(animalName, stableId);

        // Then
        assertEquals(expectedStable, actualStable);
        verify(stableRepository).findById(stableId);
        verify(stableRepository).save(expectedStable);
        verify(animalService).saveAnimal(expectedAnimal);
    }

    @Test
    public void givenAnimalName_whenGettingStableByAnimalName_thenStableWithAssignedAnimalReturned() {
        // Given
        String animalName = AnimalTest.DEFAULT_NAME;

        Animal expectedAnimal = AnimalTest.createDefaultPony();
        Stable expectedStable = StableTest.createDefaultStable();
        expectedAnimal.setStable(expectedStable);
        when(animalService.getAnimalByName(animalName)).thenReturn(expectedAnimal);

        // When
        Stable actualStable = stableService.getStableByAnimalName(animalName);

        // Then
        assertEquals(expectedStable, actualStable);
        verify(animalService).getAnimalByName(animalName);
    }

    @Test
    public void getAllStables_shouldReturnAllStables() {
        // Given
        List<Stable> expectedStables = new ArrayList<>();
        expectedStables.add(StableTest.createDefaultStable());
        expectedStables.add(StableTest.createDefaultStable());
        when(stableRepository.findAll()).thenReturn(expectedStables);

        // When
        List<Stable> actualStables = stableService.getAllStables();

        // Then
        assertEquals(expectedStables, actualStables);
        verify(stableRepository).findAll();
    }

    @Test
    public void givenStableId_whenGettingStableById_thenStableWithThatIdReturned() {
        // Given
        Long id = 1L;
        Stable expectedStable = StableTest.createDefaultStable();
        when(stableRepository.findById(id)).thenReturn(Optional.of(expectedStable));

        // When
        Stable actualStable = stableService.getStableById(id);

        // Then
        assertEquals(expectedStable, actualStable);
        verify(stableRepository).findById(id);
    }
}
