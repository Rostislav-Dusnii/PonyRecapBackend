package ucll.be.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class Address {

    private Long Id;

    @Column(name = "address_street")
    @NotBlank(message = "Street cannot be blank")
    private String street;

    @Column(name = "address_house_number")
    @Positive(message = "Number must be a positive number")
    private int number;

    @NotBlank(message = "Place cannot be blank")
    @Column(name = "address_place")
    private String place;

    public Address(String street, int number, String place) {
        setStreet(street);
        setNumber(number);
        setPlace(place);
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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
}