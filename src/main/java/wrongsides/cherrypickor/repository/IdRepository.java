package wrongsides.cherrypickor.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.domain.InventoryType;
import wrongsides.cherrypickor.domain.Region;

@Repository
public class IdRepository {

    private RestTemplate restTemplate;

    public IdRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String findItemTypeId(String name) {
        String url = String.format("https://esi.tech.ccp.is/latest/search/?datasource=tranquility&categories=inventory_type&search=%s&strict=true", name);
        InventoryType inventoryType = restTemplate.getForObject(url, InventoryType.class);
        return inventoryType.getInventoryTypeIds().get(0);
    }

    public String findRegion(String regionName) {
        String url = String.format("https://esi.tech.ccp.is/latest/search/?datasource=tranquility&categories=region&search=%s&strict=true", regionName);
        Region region = restTemplate.getForObject(url, Region.class);
        return region.getRegionIds().get(0);
    }
}
