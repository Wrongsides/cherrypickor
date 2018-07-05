package wrongsides.cherrypickor.domain;

public class Measure {

    private int value;
    private Unit unit;

    public Measure() { }

    public Measure(int value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public void setUnit(String unit) {
        this.unit = Unit.from(unit);
    }

    public Unit getUnit() {
        return unit;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
