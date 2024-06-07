package ucll.be.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ucll.be.model.Stable;
import ucll.be.service.StableService;


@RestController
@RequestMapping("/stables")
public class StableController {
    
    private StableService stableService;

    public StableController(StableService stableService) {
        this.stableService = stableService;
    }

    @GetMapping
    public List<Stable> getAllStables() {
        return stableService.getAllStables();
    }   

    @GetMapping("/{animalName}")
    public Stable getStableByAnimalName(@RequestParam(value = "animalName") String animalName) {
        return stableService.getStableByAnimalName(animalName);
    }
}
