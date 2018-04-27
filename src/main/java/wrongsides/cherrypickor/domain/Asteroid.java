package wrongsides.cherrypickor.domain;

import java.math.BigDecimal;

public class Asteroid {

    private String name;
    private int quantity;
    private Measure volume;
    private Measure distance;
    private BigDecimal value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Measure getVolume() {
        return volume;
    }

    public void setVolume(Measure volume) {
        this.volume = volume;
    }

    public Measure getDistance() {
        return distance;
    }

    public void setDistance(Measure distance) {
        this.distance = distance;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
