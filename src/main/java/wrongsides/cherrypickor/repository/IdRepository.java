package wrongsides.cherrypickor.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import wrongsides.cherrypickor.adapter.EsiAdapter;
import wrongsides.cherrypickor.domain.Category;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.domain.Region;

import java.util.List;

@Repository
public class IdRepository {

    private EsiAdapter esiAdapter;

    public IdRepository(EsiAdapter esiAdapter) {
        this.esiAdapter = esiAdapter;
    }

    @Cacheable(value = "regions")
    public String findRegionId(String regionName) {
        return esiAdapter.find(regionName, Category.REGION, Region.class).orElse(null);
    }

    public String findTypeId(String inventoryTypeName) {
        return esiAdapter.find(inventoryTypeName, Category.INVENTORY_TYPE, Item.class).orElse(null);
    }

    public String findGroupId(String typeId) {
        return esiAdapter.find(typeId, Category.TYPES).orElse(new Item()).getGroupId();
    }

    public String findCategoryId(String groupId) {
        return esiAdapter.find(groupId, Category.GROUPS).orElse(new Item()).getCategoryId();
    }

    public List<String> getGroupIds(String categoryId) {
        return esiAdapter.getCollection(categoryId, Category.CATEGORIES);
    }

    public List<String> getTypeIds(String groupId) {
        return esiAdapter.getCollection(groupId, Category.GROUPS);
    }
}
