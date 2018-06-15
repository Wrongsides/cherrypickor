package wrongsides.cherrypickor.repository;

import org.springframework.stereotype.Repository;
import wrongsides.cherrypickor.adapter.EsiAdapter;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.domain.ItemSummary;
import wrongsides.cherrypickor.domain.Region;

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

    public String setGroupId(Item item) {
        return esiAdapter.setGroupId(item,"types").getGroupId();
    }

    public String setCategoryId(Item item) {
        return esiAdapter.setCategoryId(item, "groups").getCategoryId();
    }

    public List<String> setGroupIds(String categoryId) {
        return esiAdapter.getCollection(categoryId, "categories");
    }

    public List<String> setTypeIds(String groupId) {
        return esiAdapter.getCollection(groupId, "groups");
    }

    public List<ItemSummary> getItemSummaries(List<String> typeIds) {
        return esiAdapter.getItemSummaries(typeIds);
    }
}
