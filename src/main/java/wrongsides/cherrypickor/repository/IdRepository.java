package wrongsides.cherrypickor.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.Config;
import wrongsides.cherrypickor.domain.InventoryType;
import wrongsides.cherrypickor.domain.Region;

import java.util.Optional;

@Repository
public class IdRepository {

    private Config config;
    private RestTemplate restTemplate;

    public IdRepository(Config config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public Optional<String> findItemTypeId(String name) {
        String uri = String.format("%s/%s/search/?datasource=%s&categories=inventory_type&search=%s&strict=true",
                config.getEsiUrl(), config.getEsiVersion(), config.getEsiDatasource(), name);
        InventoryType inventoryType = restTemplate.getForObject(uri, InventoryType.class);
        if (inventoryType != null) {
            return Optional.of(inventoryType.getInventoryTypeIds().get(0));
        } else {
            return Optional.empty();
        }
    }

    public Optional<String> findRegion(String regionName) {
        String url = String.format("%s/%s/search/?datasource=%s&categories=region&search=%s&strict=true",
                config.getEsiUrl(), config.getEsiVersion(), config.getEsiDatasource(), regionName);
        Region region = restTemplate.getForObject(url, Region.class);
        if(region != null) {
            return Optional.of(region.getRegionIds().get(0));
        } else {
            return Optional.empty();
        }
    }
}
