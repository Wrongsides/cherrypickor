package wrongsides.cherrypickor.domain;

public enum Category {

    INVENTORY_TYPE("inventory_type"),
    REGION("region"),
    TYPES("types"),
    GROUPS("groups"),
    CATEGORIES("categories");

    private String value;

    Category(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
