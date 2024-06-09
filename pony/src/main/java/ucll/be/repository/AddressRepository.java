package ucll.be.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ucll.be.model.Address;
import ucll.be.repository.row_mappers.AddressRowMapper;

@Repository
public class AddressRepository {
    
    private JdbcTemplate jdbcTemplate;

    public AddressRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Address save(Address address) {
        String sql = "INSERT INTO ADDRESSES (street, house_number, place, stable_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, address.getStreet(), address.getNumber(), address.getPlace());
        return address;
    }

    public List<Address> saveAll(List<Address> address) {
        for (Address a : address) {
            save(a);
        }
        return address;
    }

    public Address findById(Long id) {
        String sql = "SELECT * FROM ADDRESSES WHERE id = ?";
        return jdbcTemplate.query(sql, new AddressRowMapper(), id).get(0);
    }

    public Address findByStreetAndNumberAndPlace(String street, int number, String place) {
        String sql = "SELECT * FROM ADDRESSES WHERE street = ? AND house_number = ? AND place = ?";
        return jdbcTemplate.query(sql, new AddressRowMapper(), street, number, place).get(0);
    }

    public Address findByNumberOfAnimalsGreaterThan(int numberOfAnimals) {
        String sql = "SELECT a.* FROM ADDRESSES a JOIN MY_STABLES s ON a.id = s.address_id JOIN MY_ANIMALS an ON s.id = an.stable_id GROUP BY a.id HAVING COUNT(an.id) > ?";
        return jdbcTemplate.query(sql, new AddressRowMapper(), numberOfAnimals).get(0);
    }

}
