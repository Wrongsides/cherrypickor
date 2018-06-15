package wrongsides.cherrypickor.domain;

public class ItemSummary {

    private String category;
    private String name;
    private String id;

    public ItemSummary() {
    }

    @Override
    public String toString() {
        return "ItemSummary{" +
                "category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", typeId='" + id + '\'' +
                '}';
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
}
