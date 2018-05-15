package wrongsides.cherrypickor.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wrongsides.cherrypickor.adapter.isSearchResult;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Region implements isSearchResult {

    @JsonAlias("region")
    private List<String> ids;

    @Override
    public List<String> getIds() {
        if (ids == null) {
            ids = new ArrayList<>();
        }
        return ids;
    }
}
