package wrongsides.cherrypickor.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import wrongsides.cherrypickor.adapter.EsiAdapter;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.domain.Region;

import java.util.List;
import java.util.Optional;

@Repository
public class IdRepository {

    private EsiAdapter esiAdapter;

    public IdRepository(EsiAdapter esiAdapter) {
        this.esiAdapter = esiAdapter;
    }

    @Cacheable(value = "regions")
    public String findRegionId(String regionName) {
        return esiAdapter.find(regionName, "region", Region.class).orElse(null);
    }

    public String findItemTypeId(Item item) {
        return esiAdapter.find(item.getName(), item.getCategory(), item.getClass()).orElse(null);
    }

    public String setGroupId(Item item) {
        return esiAdapter.setGroupId(item, "types").getGroupId();
    }

    public String setCategoryId(Item item) {
        return esiAdapter.setCategoryId(item, "groups").getCategoryId();
    }

    public List<String> getGroupIds(String categoryId) {
        return esiAdapter.getCollection(categoryId, "categories");
    }

    public List<String> getTypeIds(String groupId) {
        return esiAdapter.getCollection(groupId, "groups");
    }
}
