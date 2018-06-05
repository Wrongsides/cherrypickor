package wrongsides.cherrypickor.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import wrongsides.cherrypickor.adapter.Search;

import java.util.ArrayList;
import java.util.List;

public class Item extends Search {

    @JsonAlias("inventory_type")
    private List<String> searchIds;

    private String category;
    private String name;
    private String typeId;
    private String groupId;
    private String categoryId;
    private List<String> categoryGroups;
    private List<String> categoryTypes;

    public Item() {
    }

    public Item(Category category, String name) {
        this.category = category.toString();
        this.name = name;
    }

    @Override
    public List<String> getSearchIds() {
        if (searchIds == null) {
            searchIds = new ArrayList<>();
        }
        return searchIds;
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

    @Override
    public String toString() {
        return "Item{" +
                "searchIds=" + searchIds +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", typeId='" + typeId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", categoryGroups=" + categoryGroups +
                ", categoryTypes=" + categoryTypes +
                '}';
    }
}