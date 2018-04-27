package wrongsides.cherrypicker.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypicker.domain.InventoryType;
import wrongsides.cherrypicker.domain.Region;

@Repository
public class IdRepository {

    public String findItemTypeId(String name) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("https://esi.tech.ccp.is/latest/search/?datasource=tranquility&categories=inventory_type&search=%s&strict=true", name);
        InventoryType inventoryType = restTemplate.getForObject(url, InventoryType.class);
        return inventoryType.getInventoryTypeIds().get(0);
    }

    public String findRegion(String regionName) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("https://esi.tech.ccp.is/latest/search/?datasource=tranquility&categories=region&search=%s&strict=true", regionName);
        Region region = restTemplate.getForObject(url, Region.class);
        return region.getRegionIds().get(0);
    }
}
