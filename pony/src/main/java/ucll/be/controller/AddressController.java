package ucll.be.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ucll.be.model.Address;
import ucll.be.model.Stable;
import ucll.be.service.AddressService;
import ucll.be.service.StableService;

@RestController
@RequestMapping("/address")
public class AddressController{
    
    private AddressService addressService;
    private StableService stableService;

    public AddressController(AddressService addressService, StableService stableService) {
        super();
        this.addressService = addressService;
        this.stableService = stableService;
    }

    @PostMapping
    public Address addAddress(@RequestBody Address entity) {
        return addressService.addAddress(entity);
    }
    
    @PostMapping("/stable")
    public Stable addNewStableAtNewAddress(@RequestBody Stable stable) {
        return stableService.addStable(stable);
    }

    @PutMapping("{addressId}/{stableId}")
    public Stable assignStableToAddress(@PathVariable(value = "addressId") Long addressId, @PathVariable(value = "stableId") Long stableId) {
        return stableService.assignStableToAddress(stableId, addressId);
    }

    @GetMapping("/stable/{numberOfAnimals}")
    public List<Address> getStableWithNumberOfAnimalsGreater(@PathVariable(value = "numberOfAnimals") int numberOfAnimals) {
        return addressService.getAddressWithNumberOfAnimalsGreater(numberOfAnimals);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({Exception.class})
    public Map<String, String> handleException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getClass().getSimpleName(), ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getFieldErrors()) {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }
        return errors;
    }
    
}
