package wrongsides.cherrypickor.adapter;

import java.util.ArrayList;
import java.util.List;

public interface isSearchResult {

    default List<String> getIds() {
        return new ArrayList<>();
    }
}
