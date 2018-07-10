package wrongsides.cherrypickor.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wrongsides.cherrypickor.adapter.Category;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

    private String category;
    private String name;
    @JsonAlias("type_id")
    private String typeId;
    @JsonAlias("group_id")
    private String groupId;
    @JsonAlias("category_id")
    private String categoryId;
    @JsonAlias("groups")
    private List<String> categoryGroups;
    @JsonAlias("types")
    private List<String> categoryTypes;

    public Item() {
    }

    public Item(Category category, String name) {
        this.category = category.toString();
        this.name = name;
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getCategoryGroups() {
        return categoryGroups;
    }

    public void setCategoryGroups(List<String> categoryGroups) {
        this.categoryGroups = categoryGroups;
    }

    public List<String> getCategoryTypes() {
        return categoryTypes;
    }

    public void setCategoryTypes(List<String> categoryTypes) {
        this.categoryTypes = categoryTypes;
    }
}