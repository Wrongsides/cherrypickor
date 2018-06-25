package wrongsides.cherrypickor.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import wrongsides.cherrypickor.adapter.EsiAdapter;
import wrongsides.cherrypickor.domain.Category;
import wrongsides.cherrypickor.domain.Item;

@Component
public class ItemRepository {

    private EsiAdapter esiAdapter;

    public ItemRepository(EsiAdapter esiAdapter) {
        this.esiAdapter = esiAdapter;
    }

    @Cacheable(value = "items")
    public Item getByName(String name) {
        return esiAdapter.getByName(name);
    }

    @CachePut(value = "items", key = "#result.name", condition = "#result != null")
    public Item getByTypeId(String typeId) {
        return esiAdapter.find(typeId, Category.TYPES).orElse(new Item());
    }

    @CacheEvict(value = "items", allEntries = true)
    public boolean removeAll() {
        return true;
    }
}
