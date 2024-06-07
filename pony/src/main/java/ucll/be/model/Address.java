package ucll.be.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "ADDRESSES")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Street cannot be blank")
    private String street;

    @Positive(message = "Number must be a positive number")
    @Column(name = "house_number")
    private int number;

    @NotBlank(message = "Place cannot be blank")
    private String place;

    @OneToOne(mappedBy = "address")
    @JsonBackReference
    private Stable stable;

    protected Address() {
        // Empty constructor for JPA
    }

    public Address(String street, int number, String place) {
        setStreet(street);
        setNumber(number);
        setPlace(place);
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Stable getStable() {
        return stable;
    }

    public void setStable(Stable stable) {
        if (this.stable != null) {
            throw new IllegalArgumentException("Address is already assigned to a stable");
        }
        this.stable = stable;
    }
}