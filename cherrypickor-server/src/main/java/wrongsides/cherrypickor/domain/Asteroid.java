package wrongsides.cherrypickor.domain;

import java.math.BigDecimal;

public class Asteroid {

    private String name;
    private int quantity;
    private Measure volume;
    private Measure distance;
    private BigDecimal value;

    private Asteroid() { }

    private Asteroid(String name, int quantity, Measure volume, Measure distance, BigDecimal value) {
        this.name = name;
        this.quantity = quantity;
        this.volume = volume;
        this.distance = distance;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public static AsteroidBuilder of(String name) {
        return new AsteroidBuilder(name);
    }

    public static class AsteroidBuilder {
        private String name;
        private int quantity;
        private Measure volume;
        private Measure distance;
        private BigDecimal value;

        private AsteroidBuilder(String name) {
            this.name = name;
        }

        public AsteroidBuilder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public AsteroidBuilder withVolume(Measure volume) {
            this.volume = volume;
            return this;
        }

        public AsteroidBuilder withDistance(Measure distance) {
            this.distance = distance;
            return this;
        }

        public AsteroidBuilder withValue(BigDecimal value) {
            this.value = value;
            return this;
        }

        public Asteroid build() {
            return new Asteroid(name, quantity, volume, distance, value);
        }
    }
}
