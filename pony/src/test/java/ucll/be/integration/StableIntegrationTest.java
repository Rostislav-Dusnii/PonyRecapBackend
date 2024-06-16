package ucll.be.integration;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import ucll.be.model.Stable;
import ucll.be.repository.DbInitializer;
import ucll.be.repository.StableRepository;
import ucll.be.utilits.TimeTracker;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient()
@Sql("classpath:schema.sql")
public class StableIntegrationTest {
        
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private StableRepository stableRepository;
    @Autowired
    private DbInitializer dbInitializer;

    @BeforeEach
    public void setupDatabasesAndTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
        dbInitializer.populateDB();
    }

    @Test
    public void givenNoStables_whenGetAllStables_thenEmptyListReturned() {
        int expectedSize = stableRepository.findAll().size();

        webTestClient.get()
                .uri("/stables")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Stable.class)
                .hasSize(expectedSize);
    }

    @Test
    public void givenAnimalName_whenGetStableByAnimalName_thenStableReturned() {
        String animalName = "Luna";
        List<Stable> expectedStables = stableRepository.findAll();
        Stable expectedStable = expectedStables.stream().filter(s -> s.getAnimals().stream().anyMatch(a -> a.getName().equals(animalName))).findFirst().orElse(null);

        webTestClient.get()
                .uri("/stables/{animalName}", animalName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Stable.class)
                .value(stable -> {
                stable.getName().equals(expectedStable.getName()); 
                });
    }
}
