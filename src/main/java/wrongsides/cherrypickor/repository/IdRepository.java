package wrongsides.cherrypickor.repository;

import org.springframework.stereotype.Repository;
import wrongsides.cherrypickor.adapter.EsiAdapter;
import wrongsides.cherrypickor.domain.InventoryType;
import wrongsides.cherrypickor.domain.Region;

import java.util.Optional;

@Repository
public class IdRepository {

    private EsiAdapter esiAdapter;

    public IdRepository(EsiAdapter esiAdapter) {
        this.esiAdapter = esiAdapter;
    }

    public Optional<String> findItemTypeId(String itemName) {
        return esiAdapter.find(itemName, "inventory_type", InventoryType.class);
    }

    public Optional<String> findRegion(String regionName) {
        return esiAdapter.find(regionName, "region", Region.class);
    }
}
