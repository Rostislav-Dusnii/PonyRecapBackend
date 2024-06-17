package ucll.be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ucll.be.model.Animal;
import ucll.be.model.Chicken;
import ucll.be.model.Pony;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    // You can add custom query methods here if needed

    Animal findByNameIgnoreCase(String name);
    List<Pony> findByAgeGreaterThan(int age);
    Pony findFirstByOrderByAgeDesc();
    @Query("SELECT mr.animal FROM MedicalRecord mr WHERE mr.closingDate IS NULL")
    List<Animal> findAnimalsWithOpenMedicalRecords();

    @Query("SELECT a FROM Animal a WHERE a.animalType = 'Pony'")
    List<Pony> findAllPonies();

    @Query("SELECT a FROM Animal a WHERE a.animalType = 'Chicken'")
    List<Chicken> findAllChickens();

    @Query("SELECT a FROM Chicken a WHERE a.animalType = 'Chicken' AND a.laysEggs = TRUE")
    List<Chicken> findAllChickensThatLayEggs();
}