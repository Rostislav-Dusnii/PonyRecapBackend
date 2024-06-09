package ucll.be.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Embeddable
public class Address {

    @NotBlank(message = "Street cannot be blank")
    private String street;

    @Positive(message = "Number must be a positive number")
    private int number;

    @NotBlank(message = "Place cannot be blank")
    private String place;

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
        if (this.stable != null && this.stable.getId() != null) {
            throw new IllegalArgumentException("Address is already assigned to a stable");
        }
        this.stable = stable;
    }
}