package wrongsides.cherrypickor.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wrongsides.cherrypickor.adapter.Search;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item extends Search {

    @JsonAlias("inventory_type")
    private List<String> ids;

    private String category;
    private String name;
    private String id;

    public List<String> getIds() {
        if (ids == null) {
            ids = new ArrayList<>();
        }
        return ids;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Item{" +
                "ids=" + ids +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
