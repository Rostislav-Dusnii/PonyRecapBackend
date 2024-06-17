package ucll.be.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "MY_ANIMALS")
@DiscriminatorValue("Chicken")
public class Chicken extends Animal
{
    private Boolean laysEggs;

    protected Chicken() {
        super();
    }

    public Chicken(String name, Boolean laysEggs) {
        super(name);
        setLaysEggs(laysEggs);
    }

    public Boolean getLaysEggs() {
        return laysEggs;
    }

    public void setLaysEggs(Boolean laysEggs) {
        this.laysEggs = laysEggs;
    }
}
