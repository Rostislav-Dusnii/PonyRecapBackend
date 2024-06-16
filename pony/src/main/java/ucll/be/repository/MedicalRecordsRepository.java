package ucll.be.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ucll.be.model.MedicalRecord;

public interface MedicalRecordsRepository extends JpaRepository<MedicalRecord, Long> {
    
    List<MedicalRecord> findByAnimalIdAndRegistrationDateAfter(Long animalId, LocalDate registerDate);

    Boolean existsByRegistrationDateAndDescriptionAndAnimalId(LocalDate registrationDate, String description, Long animalId);
}