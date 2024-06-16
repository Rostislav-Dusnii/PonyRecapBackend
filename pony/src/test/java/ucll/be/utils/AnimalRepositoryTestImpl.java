package ucll.be.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import ucll.be.model.Animal;
import ucll.be.model.MedicalRecord;
import ucll.be.repository.AnimalRepository;
import ucll.be.repository.DbInitializer;

public class AnimalRepositoryTestImpl implements AnimalRepository{

    public List<Animal> animals;
    public List<MedicalRecord> medicalRecords;

    public AnimalRepositoryTestImpl() {
        resetRepository();
    }

    public void resetRepository(){
        this.animals = DbInitializer.createAnimals();
        this.medicalRecords = DbInitializer.createMedicalRecords(animals);
        for (int i = 0; i < animals.size(); i++) {
            animals.get(i).setId(Long.valueOf(i));
        }
        for (int i = 0; i < medicalRecords.size(); i++) {
            medicalRecords.get(i).setId(Long.valueOf(i));
        }
    }

    @Override
    public Animal findByNameIgnoreCase(String name) {
        Animal result = animals.stream().filter(a -> a.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        return result;
    }

    @Override
    public List<Animal> findByAgeGreaterThan(int age) {
        List<Animal> result = animals.stream().filter(a -> a.getAge() > age).toList();
        return result;
    }

    @Override
    public Animal findFirstByOrderByAgeDesc() {
        Animal result = animals.stream().max((a1, a2) -> a1.getAge() - a2.getAge()).orElse(null);
        return result;
    }

    @Override
    public List<Animal> findAnimalsWithOpenMedicalRecords() {
        List<Animal> result = medicalRecords.stream().filter(mr -> mr.getClosingDate() == null).map(mr -> mr.getAnimal()).toList();
        return result;
    }

    
    @Override
    public List<Animal> findAll() {
        return animals;
    }
    @Override
    public <S extends Animal> S save(S entity) {
        if (entity.getId() == null) {

            entity.setId(Long.valueOf(animals.size()));
        }
        animals.add(entity);
        return entity;
    }

    @Override
    public Optional<Animal> findById(Long id) {
        Animal result = animals.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Long id) {
        return animals.stream().anyMatch(a -> a.getId().equals(id));
    }


    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends Animal> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends Animal> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<Animal> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllByIdInBatch'");
    }

    @Override
    public void deleteAllInBatch() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public Animal getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public Animal getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Animal getReferenceById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }

    @Override
    public <S extends Animal> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Animal> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Animal> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }


    @Override
    public List<Animal> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public void delete(Animal entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends Animal> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<Animal> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Page<Animal> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Animal> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends Animal> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Animal> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends Animal> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends Animal, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }
    
}
