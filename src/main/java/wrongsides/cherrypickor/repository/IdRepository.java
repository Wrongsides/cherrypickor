package wrongsides.cherrypickor.repository;

import org.springframework.stereotype.Repository;
import wrongsides.cherrypickor.adapter.EsiAdapter;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.domain.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class IdRepository {

    private EsiAdapter esiAdapter;

    public IdRepository(EsiAdapter esiAdapter) {
        this.esiAdapter = esiAdapter;
    }


    public Optional<String> findRegionId(String regionName) {
        return esiAdapter.find(regionName, "region", Region.class);
    }

    public Optional<String> findItemTypeId(Item item) {
        return esiAdapter.find(item.getName(), item.getCategory(), item.getClass());
    }

    public Optional<String> findGroupId(Item item) {
        return esiAdapter.find(item.getTypeId(), "types", Item.class);
    }

    public Optional<String> findCategoryId(Item item) {
        return esiAdapter.find(item.getTypeId(), "types", Item.class);
    }

    public List<String> findGroupIds(String categoryId) {
        return esiAdapter.get(categoryId, "categories").orElse(new ArrayList<>());
    }

    public List<String> findTypeIds(String groupId) {
        return esiAdapter.get(groupId, "groups").orElse(new ArrayList<>());
    }
}
