package ucll.be.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import ucll.be.model.Animal;
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
        return medicalRecordsRepository.findById(id).orElseThrow(() -> new ServiceException("MedicalRecord with id " + id + " does not exist"));
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        Animal animal = animalService.getAnimalById(medicalRecord.getAnimal().getId());
        medicalRecord.setAnimal(animal);
        
        return medicalRecordsRepository.save(medicalRecord);
    }

    public MedicalRecord closeMedicalRecord(Long id, LocalDate closingDate) {
        MedicalRecord medicalRecord = getMedicalRecordById(id);
        medicalRecord.setClosingDate(closingDate);
        return medicalRecordsRepository.save(medicalRecord);
    }

    public List<MedicalRecord> getByAnimalIdAndAfterDate(Long animalId, LocalDate registerDate) {
        return medicalRecordsRepository.findByAnimalIdAndRegistrationDateAfter(animalId, registerDate);
    }
}
