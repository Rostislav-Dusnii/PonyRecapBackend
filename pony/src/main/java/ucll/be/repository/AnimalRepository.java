package ucll.be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ucll.be.model.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    // You can add custom query methods here if needed

    Animal findByNameIgnoreCase(String name);
    List<Animal> findByAgeGreaterThan(int age);
    Animal findFirstByOrderByAgeDesc();
    @Query("SELECT mr.animal FROM MedicalRecord mr WHERE mr.closingDate IS NULL")
    List<Animal> findAnimalsWithOpenMedicalRecords();
}