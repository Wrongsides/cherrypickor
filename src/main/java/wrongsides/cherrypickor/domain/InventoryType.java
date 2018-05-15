package wrongsides.cherrypickor.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wrongsides.cherrypickor.adapter.isSearchResult;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryType implements isSearchResult {

    public InventoryType() { }

    @JsonAlias("inventory_type")
    private List<String> ids;

    public List<String> getIds() {
        if (ids == null) {
            ids = new ArrayList<>();
        }
        return ids;
    }
}
