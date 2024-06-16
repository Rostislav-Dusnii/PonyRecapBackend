package ucll.be.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ucll.be.domain.AddressTest;
import ucll.be.model.Address;
import ucll.be.repository.AddressRepository;
import ucll.be.utilits.TimeTracker;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;
    
    @BeforeEach
    public void resetTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
    }

    @Test
    public void testGetAddressById_ExistingAddress_ReturnsAddress() {
        // Given
        Long id = 1L;
        Address expectedAddress = AddressTest.createDefaultAddress();
        expectedAddress.setId(id);
        when(addressRepository.findById(id)).thenReturn(expectedAddress);

        // When
        Address actualAddress = addressService.getAddressById(id);

        // Then
        assertEquals(expectedAddress, actualAddress);
        verify(addressRepository).findById(id);
    }

    @Test
    public void testGetAddressById_NonExistingAddress_ThrowsServiceException() {
        // Given
        Long id = 1L;
        when(addressRepository.findById(id)).thenReturn(null);

        // When/Then
        assertThrows(ServiceException.class, () -> addressService.getAddressById(id));
        verify(addressRepository).findById(id);
    }

    @Test
    public void testGetAddressWithNumberOfAnimalsGreater_ReturnsMatchingAddresses() {
        // Given
        int numberOfAnimals = 5;
        List<Address> expectedAddresses = new ArrayList<>();
        expectedAddresses.add(AddressTest.createDefaultAddress());
        expectedAddresses.add(AddressTest.createDefaultAddress());
        when(addressRepository.findByNumberOfAnimalsGreaterThan(numberOfAnimals)).thenReturn(expectedAddresses);

        // When
        List<Address> actualAddresses = addressService.getAddressWithNumberOfAnimalsGreater(numberOfAnimals);

        // Then
        assertEquals(expectedAddresses, actualAddresses);
        verify(addressRepository).findByNumberOfAnimalsGreaterThan(numberOfAnimals);
    }
}
