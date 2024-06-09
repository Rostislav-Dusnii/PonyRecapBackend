package ucll.be.repository.row_mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import io.micrometer.common.lang.NonNull;
import ucll.be.model.Address;

public class AddressRowMapper implements RowMapper<Address> {

    @Override
    public Address mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String street = rs.getString("street");
        int number = rs.getInt("house_number");
        String place = rs.getString("place");

        Address address = new Address(street, number, place);
        address.setId(id);
        return address;
    }
}