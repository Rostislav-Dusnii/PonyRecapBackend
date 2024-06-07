package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    // You can add custom query methods here if needed

    Animal findByName(String name);
}