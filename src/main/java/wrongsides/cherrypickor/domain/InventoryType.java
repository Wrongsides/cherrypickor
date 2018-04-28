package wrongsides.cherrypickor.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryType {

    @JsonAlias("inventory_type")
    private List<String> inventoryTypeIds;

    public List<String> getInventoryTypeIds() {
        if (inventoryTypeIds == null) {
            inventoryTypeIds = new ArrayList<>();
        }
        return inventoryTypeIds;
    }
}
