package ucll.be.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ucll.be.model.Address;
import ucll.be.model.Stable;
import ucll.be.service.AddressService;
import ucll.be.service.StableService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/address")
public class AddressController {
    
    private AddressService addressService;
    private StableService stableService;

    public AddressController(AddressService addressService, StableService stableService) {
        this.addressService = addressService;
        this.stableService = stableService;
    }

    @PostMapping
    public void addAddress(@RequestBody Address entity) {
        addressService.addAddress(entity);
    }
    
    @PostMapping("/stable")
    public void addNewStableAtNewAddress(@RequestBody Stable stable) {
        stableService.addStable(stable);
    }

    @PutMapping("{addressId}/{stableId}")
    public void assignStableToAddress(@PathVariable(value = "addressId") Long addressId, @PathVariable(value = "stableId") Long stableId) {
        stableService.assignStableToAddress(stableId, addressId);
    }

    @GetMapping("/stable/{numberOfAnimals}")
    public Address getStableWithNumberOfAnimalsGreater(@RequestParam(value = "numberOfAnimals") int numberOfAnimals) {
        return addressService.getAddressWithNumberOfAnimalsGreater(numberOfAnimals);
    }
    
}
