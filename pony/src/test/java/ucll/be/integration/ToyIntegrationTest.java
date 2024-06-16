package ucll.be.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import ucll.be.domain.ToyTest;
import ucll.be.model.Toy;
import ucll.be.repository.DbInitializer;
import ucll.be.repository.ToyRepository;
import ucll.be.utilits.TimeTracker;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient()
@Sql("classpath:schema.sql")
public class ToyIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ToyRepository toyRepository;
    @Autowired
    private DbInitializer dbInitializer;

    @BeforeEach
    public void setupDatabasesAndTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
        dbInitializer.populateDB();
    }

    @Test
    public void givenToyName_whenGetToyByName_thenReturnToy() {
        Toy expectedToy = toyRepository.findAll().get(0);

        webTestClient.get()
                .uri("/toys/{name}", expectedToy.getName()) 
                .exchange()
                .expectStatus().isOk()
                .expectBody(Toy.class)
                .value(toy -> assertEquals(expectedToy.getName(), toy.getName()));
    }

    @Test
    public void givenNewToy_whenAddToy_thenReturnAddedToy() {
        Toy toy = ToyTest.createDefaultToy();

        webTestClient.post()
                .uri("/toys")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(toy)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Toy.class)
                .value(addedToy -> assertEquals(toy.getName(), addedToy.getName()));
    }

    @Test
    public void givenToyIdAndAnimalId_whenAddAnimalToToy_thenReturnUpdatedToy() {
        Long toyId = 1L;
        Long animalId = 3L;

        webTestClient.put()
                .uri("/toys/{toyId}/animal/{animalId}", toyId, animalId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Toy.class)
                .value(updatedToy -> {
                    assertEquals(toyId, updatedToy.getId());
                    assertTrue(updatedToy.getAnimals().stream().anyMatch(animal -> animal.getId() == animalId));
                });
    }
}
