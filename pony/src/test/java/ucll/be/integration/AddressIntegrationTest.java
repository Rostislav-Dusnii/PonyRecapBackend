package ucll.be.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import ucll.be.domain.AddressTest;
import ucll.be.domain.StableTest;
import ucll.be.model.Address;
import ucll.be.model.Stable;
import ucll.be.repository.AddressRepository;
import ucll.be.repository.DbInitializer;
import ucll.be.utilits.TimeTracker;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient()
@Sql("classpath:schema.sql")
public class AddressIntegrationTest {
        
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private DbInitializer dbInitializer;

    @BeforeEach
    public void setupDatabasesAndTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
        dbInitializer.populateDB();
    }

    @Test
    public void givenAddress_whenAddAddress_thenAddressAdded() {
        Address address = AddressTest.createDefaultAddress();

        webTestClient.post()
                .uri("/address")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(address)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("{street: '" + address.getStreet() + "', number: " + address.getNumber() + "}");
    }

    @Test
    public void givenStable_whenAddNewStableAtNewAddress_thenStableAdded() {
        Address address = AddressTest.createDefaultAddress();

        webTestClient.post()
                .uri("/address")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(address)
                .exchange()
                .expectStatus().isOk();

                
        Long expectedAddressId = Long.valueOf(addressRepository.findAll().size());
        Stable stable = StableTest.createDefaultStable();
        stable.setAddressId(expectedAddressId);

        webTestClient.post()
                .uri("/address/stable")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(stable)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("{name: '" + stable.getName() + "', animals: [], addressId: " + expectedAddressId + "}");  
    }

    @Test
    public void givenNumberOfAnimals_whenGetStableWithNumberOfAnimalsGreater_thenStablesReturned() {
        int numberOfAnimals = 0;

        webTestClient.get()
                .uri("/address/stable/{numberOfAnimals}", numberOfAnimals)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Address.class)
                .hasSize(2);
    }
}
