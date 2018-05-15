package wrongsides.cherrypickor.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.Config;
import wrongsides.cherrypickor.domain.InventoryType;
import wrongsides.cherrypickor.domain.Region;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Repository
public class IdRepository {

    private Config config;
    private RestTemplate restTemplate;

    public IdRepository(Config config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public Optional<String> findItemTypeId(String itemName) {
        InventoryType inventoryType = find(itemName, "inventory_type", InventoryType.class);
        if (inventoryType == null) {
            return Optional.empty();
        }
        return getFirstString(inventoryType::getInventoryTypeIds);
    }

    public Optional<String> findRegion(String regionName) {
        Region region = find(regionName, "region", Region.class);
        if (region == null) {
            return Optional.empty();
        }
        return getFirstString(region::getRegionIds);
    }

    private <T> T find(String name, String category, Class<T> clazz) {
        String url = String.format("%s/%s/search/?datasource=%s&categories=%s&search=%s&strict=true",
                config.getEsiUrl(), config.getEsiVersion(), config.getEsiDatasource(), category, name);
        return restTemplate.getForObject(url, clazz);
    }

    private Optional<String> getFirstString(Supplier<List<String>> supplier) {
        if(supplier.get().isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(supplier.get().get(0));
        }
    }
}
