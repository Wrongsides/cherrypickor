package wrongsides.cherrypicker.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Region {

    @JsonAlias("region")
    private List<String> regionIds;

    public List<String> getRegionIds() {
        return regionIds == null ? new ArrayList<>() : regionIds;
    }
}
