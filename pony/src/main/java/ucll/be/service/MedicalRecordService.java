package ucll.be.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import ucll.be.model.MedicalRecord;
import ucll.be.repository.MedicalRecordsRepository;

@Service
public class MedicalRecordService {
    
    private MedicalRecordsRepository medicalRecordsRepository;
    private AnimalService animalService;

    public MedicalRecordService(MedicalRecordsRepository medicalRecordsRepository, AnimalService animalService) {
        this.medicalRecordsRepository = medicalRecordsRepository;
        this.animalService = animalService;
    }

    public MedicalRecord getMedicalRecordById(Long id) {
        return medicalRecordsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("MedicalRecord with id " + id + " does not exist"));
    }

    // public MedicalRecord createAndAddMedicalRecord(LocalDate registrationDate, String description, Long animalId) {
    //     Animal animal = animalService.getAnimalById(animalId);

    //     MedicalRecord medicalRecord = new MedicalRecord(registrationDate, description, animal);
    //     return addMedicalRecord(medicalRecord);
    // }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        throwErrorIfExists(medicalRecord);
        return medicalRecordsRepository.save(medicalRecord);
    }

    public MedicalRecord closeMedicalRecord(Long id, LocalDate closingDate) {
        MedicalRecord medicalRecord = getMedicalRecordById(id);
        medicalRecord.setClosingDate(closingDate);
        return medicalRecordsRepository.save(medicalRecord);
    }

    public MedicalRecord getByAnimalIdAndAfterDate(Long animalId, LocalDate registerDate) {
        return medicalRecordsRepository.findByAnimalIdAndRegistrationDateAfter(animalId, registerDate);
    }

    public void throwErrorIfExists(MedicalRecord medicalRecord) {
        if (medicalRecordsRepository.existsByRegistrationDateAndDescriptionAndAnimalId(medicalRecord.getRegistrationDate(), medicalRecord.getDescription(), medicalRecord.getAnimal().getId())) {
            throw new IllegalArgumentException("Meidcal record with registration date " + medicalRecord.getRegistrationDate() + " and description " + medicalRecord.getDescription() + " already exists");
        }
    }

}
