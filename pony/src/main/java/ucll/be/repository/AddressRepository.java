package ucll.be.repository;

import java.util.ArrayList;
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
        String sql = "INSERT INTO ADDRESSES (street, house_number, place) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, address.getStreet(), address.getNumber(), address.getPlace());
        return findByStreetAndNumberAndPlace(address.getStreet(), address.getNumber(), address.getPlace());
    }

    public List<Address> saveAll(List<Address> address) {
        List<Address> savedAddresses = new ArrayList<>();
        for (Address a : address) {
            Address savedAddress = save(a);
            savedAddresses.add(savedAddress);
        }
        return savedAddresses;
    }

    public Address findById(Long id) {
        String sql = "SELECT * FROM ADDRESSES WHERE id = ?";
        List<Address> addresses = jdbcTemplate.query(sql, new AddressRowMapper(), id);
        if (addresses.isEmpty()) {
            return null;
        }
        return addresses.get(0);
    }

    public Address findByStreetAndNumberAndPlace(String street, int number, String place) {
        String sql = "SELECT * FROM ADDRESSES WHERE street = ? AND house_number = ? AND place = ?";
        List<Address> addresses = jdbcTemplate.query(sql, new AddressRowMapper(), street, number, place);
        if (addresses.isEmpty()) {
            return null;
        }
        return addresses.get(0);
    }

    public List<Address> findByNumberOfAnimalsGreaterThan(int numberOfAnimals) {
        String sql = "SELECT * FROM ADDRESSES WHERE (SELECT COUNT(*) FROM MY_ANIMALS a INNER JOIN MY_STABLES s ON a.stable_id = s.id WHERE s.address_id = ADDRESSES.id) > ?";
        return jdbcTemplate.query(sql, new AddressRowMapper(), numberOfAnimals);
    }

    public List<Address> findAll() {
        String sql = "SELECT * FROM ADDRESSES";
        return jdbcTemplate.query(sql, new AddressRowMapper());
    }

}
