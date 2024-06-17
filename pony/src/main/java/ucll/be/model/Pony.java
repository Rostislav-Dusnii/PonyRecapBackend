package ucll.be.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


@Entity
@Table(name = "MY_ANIMALS")
@DiscriminatorValue("Pony")
public class Pony extends Animal{

    @Min(value = 1, message = "Age must be greater than or equal to 1")
    @Max(value = 50, message = "Age must be less than or equal to 50")
    private int age;

    protected Pony() {
        super();
    }

    public Pony(String name, int age) {
        super(name);
        setAge(age);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
