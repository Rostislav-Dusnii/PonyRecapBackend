package ucll.be.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ucll.be.model.Toy;

@Repository
public interface ToyRepository extends JpaRepository<Toy, Long> {
    Boolean existsByNameIgnoreCase(String name);
    Optional<Toy> findByNameIgnoreCase(String name);
}


