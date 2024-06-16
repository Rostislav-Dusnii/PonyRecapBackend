package ucll.be.integration;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import ucll.be.domain.MedicalRecordTest;
import ucll.be.model.Animal;
import ucll.be.model.MedicalRecord;
import ucll.be.repository.AnimalRepository;
import ucll.be.repository.DbInitializer;
import ucll.be.repository.MedicalRecordsRepository;
import ucll.be.utilits.TimeTracker;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient()
@Sql("classpath:schema.sql")
public class MedicalRecordIntegrationTest {
    
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private MedicalRecordsRepository medicalRecordsRepository;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private DbInitializer dbInitializer;

    @BeforeEach
    public void setupDatabasesAndTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
        dbInitializer.populateDB();
    }

    @Test
    public void givenAnimalIdAndRegisterDate_whenGetByAnimalIdAndAfterDate_thenReturnMedicalRecords() {
        Long animalId = 1L;
        LocalDate registerDate = TimeTracker.getToday().minusMonths(1);
        int expectedSize = medicalRecordsRepository.findAll().stream().filter(md -> md.getAnimal().getId() == animalId && md.getRegistrationDate().isAfter(registerDate)).toList().size();

        webTestClient.get()
                .uri("/medicalrecord/{animalId}/{registerDate}", animalId, registerDate)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MedicalRecord.class)
                .hasSize(expectedSize);
    }

    @Test
    public void givenMedicalRecord_whenAddMedicalRecord_thenReturnAddedMedicalRecord() {
        MedicalRecord medicalRecord = MedicalRecordTest.createDefaultMedicalRecord();
        Animal animal = animalRepository.findById(1L).orElse(null);
        medicalRecord.setAnimal(animal);

        // Set medical record properties

        webTestClient.post()
                .uri("/medicalrecord")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(medicalRecord)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MedicalRecord.class)
                .value(md -> {
                    md.getDescription().equals(medicalRecord.getDescription());
                    md.getRegistrationDate().equals(medicalRecord.getRegistrationDate());
                    md.getAnimal().equals(medicalRecord.getAnimal());
                });
    }

    @Test
    public void givenMedicalRecordIdAndClosingDate_whenCloseMedicalRecord_thenReturnClosedMedicalRecord() {
        Long medicalRecordId = 1L;
        LocalDate closingDate = TimeTracker.getToday();

        webTestClient.put()
                .uri("/medicalrecord/{medicalRecordId}/{closingDate}", medicalRecordId, closingDate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MedicalRecord.class);
    }
}
