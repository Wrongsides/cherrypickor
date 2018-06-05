package wrongsides.cherrypickor.domain;

public enum Category {

    INVENTORY_TYPE("inventory_type");

    private String value;

    Category(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
