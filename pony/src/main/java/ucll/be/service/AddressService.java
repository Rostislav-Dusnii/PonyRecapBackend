package ucll.be.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ucll.be.model.Address;
import ucll.be.repository.AddressRepository;

@Service
public class AddressService {
    
    private AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address addAddress(Address address) {
        throwErrorIfExists(address.getStreet(), address.getNumber(), address.getPlace());
        return addressRepository.save(address);
    }

    public Address getAddressById(Long id) {
        Address address = addressRepository.findById(id);
        if (address == null) {
            throw new ServiceException("Address with id " + id + " does not exist");
        }

        return address;
    }

    public List<Address> getAddressWithNumberOfAnimalsGreater(int numberOfAnimals) {
        return addressRepository.findByNumberOfAnimalsGreaterThan(numberOfAnimals);
    }

    public void throwIfDoesNotExist(Long id) {
        if (addressRepository.findById(id) == null) {
            throw new ServiceException("Address with id " + id + " does not exist");
        }
    }

    public void throwErrorIfExists(String street, int number, String place) {
        if (addressRepository.findByStreetAndNumberAndPlace(street, number, place) != null) {
            throw new ServiceException("Address with street " + street + " and number " + number + " and place " + place + " already exists");
        }
    }
}
