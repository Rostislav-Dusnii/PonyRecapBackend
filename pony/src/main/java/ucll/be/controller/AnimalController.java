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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ucll.be.model.Animal;
import ucll.be.model.Stable;
import ucll.be.service.AnimalService;
import ucll.be.service.StableService;



@RestController
@RequestMapping("/animals")
public class AnimalController {
    
    private AnimalService service;
    private StableService stableService;

    public AnimalController(AnimalService service, StableService stableService) {
        this.service = service;
        this.stableService = stableService;
    }

    @PostMapping
    public Animal addAnimal(@RequestBody Animal entity) {
        return service.addAnimal(entity);
    }

    @PostMapping("/{animalName}/stable")
    public Stable assignAnimalToStable(@PathVariable(value = "animalName") String animalName, @RequestBody Stable stable) {
        return stableService.assignAnimalToStable(animalName, stable);
    }
    
    @PostMapping("/{animalName}")
    public Stable removeAnimalFromStable(@PathVariable(value = "animalName") String animalName,
    @RequestParam(value = "stableId") Long stableId) {
        return stableService.assignAnimalToStable(animalName, stableId);
    }

    @GetMapping
    public List<Animal> getAllAnimals() {
        return service.getAllAnimals();
    }

    @GetMapping("/{age}")
    public List<Animal> getAnimalsOlderThan(@PathVariable(value = "age") int age) {
        return service.getAnimalsOlderThan(age);
    }

    @GetMapping("/oldest")
    public Animal getOldestAnimal() {
        return service.getOldestAnimal();
    }

    @GetMapping("/openmedicalrecords")
    public List<Animal> getAnimalsWithOpenMedicalRecords() {
        return service.getAnimalsWithOpenMedicalRecords();
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
