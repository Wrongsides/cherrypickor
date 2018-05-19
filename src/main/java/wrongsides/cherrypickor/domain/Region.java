package wrongsides.cherrypickor.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wrongsides.cherrypickor.adapter.Search;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Region extends Search {

    @JsonAlias("region")
    private List<String> ids;

    public List<String> getIds() {
        if (ids == null) {
            ids = new ArrayList<>();
        }
        return ids;
    }
}
