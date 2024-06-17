package ucll.be.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import ucll.be.domain.AnimalTest;
import ucll.be.domain.StableTest;
import ucll.be.model.Animal;
import ucll.be.model.Pony;
import ucll.be.model.Stable;
import ucll.be.repository.AnimalRepository;
import ucll.be.repository.DbInitializer;
import ucll.be.repository.MedicalRecordsRepository;
import ucll.be.repository.StableRepository;
import ucll.be.utilits.TimeTracker;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient()
@Sql("classpath:schema.sql")
public class AnimalIntegrationTest {
        
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private StableRepository stableRepository;
    @Autowired 
    private MedicalRecordsRepository medicalRecordsRepository;
    @Autowired
    private DbInitializer dbInitializer;

    @BeforeEach
    public void setupDatabasesAndTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
        dbInitializer.populateDB();
    }

    @Test
    public void givenAnimal_whenAddAnimal_thenAnimalAdded() {
        Pony animal = AnimalTest.createDefaultPony();
        
        webTestClient.post()
                .uri("/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(animal)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("{name: '" + animal.getName() + "', age: " + animal.getAge() + "}");
    }

    @Test
    public void givenAnimalAndStable_whenAssignAnimalToStable_thenAnimalAssignedToStable() {
        Pony animal = new Pony("Little", 5);
        Stable stable = StableTest.createDefaultStable();
        
        webTestClient.post()
                    .uri("/animals/" + animal.getName() + "/stable")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(stable)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json("{name: '" + stable.getName() + "', animals: [{name: '" + animal.getName() + "'} ]}");
    }

    @Test
    public void givenAnimalAndStableId_whenAssingAnimalToExistingStable_thenAnimalAssignedToStable() {
        Pony animal = new Pony("Little", 5);
        Long stableId = 1L;
        Stable stable = stableRepository.findById(stableId).orElse(null);

        
        webTestClient.post()
                .uri(String.format("/animals/%s?stableId=%d", animal.getName(), stableId))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("{name: '" + stable.getName() + "', animals: [{name: '" + animal.getName() + "'}, {} ]}");
    }

    @Test
    public void givenNoParameters_whenGetAllAnimals_thenAllAnimalsReturned() {
        int expectedSize = animalRepository.findAll().size();
        
        webTestClient.get()
                .uri("/animals")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Animal.class)
                .hasSize(expectedSize);
    }

    @Test
    public void givenAge_whenGetAnimalsOlderThan_thenAnimalsOlderThanAgeReturned() {
        int age = 3;
        int expectedSize = animalRepository
        .findAll()
        .stream()
        .filter(a -> a.getClass().equals(Pony.class))
        .map(a -> (Pony) a)
        .filter(a -> a.getAge() > age)
        .toList()
        .size();
        
        webTestClient.get()
                .uri("/animals/{age}", age)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Animal.class)
                .hasSize(expectedSize);
    }

    @Test
    public void givenNoParameters_whenGetOldestAnimal_thenOldestAnimalReturned() {
        Pony expecteAnimal = animalRepository
        .findAll()
        .stream()
        .filter(a -> a.getClass().equals(Pony.class))
        .map(a -> (Pony) a)
        .max((a1, a2) -> Integer.compare(a1.getAge(), a2.getAge()))
        .orElse(null);

        webTestClient.get()
                .uri("/animals/oldest")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("{name: '" + expecteAnimal.getName() + "', age: " + expecteAnimal.getAge() + "}");
    }

    @Test
    public void givenNoParameters_whenGetAnimalsWithOpenMedicalRecords_thenAnimalsWithOpenMedicalRecordsReturned() {
        int expectedSize = medicalRecordsRepository.findAll().stream().filter(mr -> mr.getClosingDate() == null).map(mr -> mr.getAnimal()).toList().size();

        webTestClient.get()
                .uri("/animals/openmedicalrecords")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Animal.class)
                .hasSize(expectedSize);
    }
}
