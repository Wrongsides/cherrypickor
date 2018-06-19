package wrongsides.cherrypickor.repository;

import wrongsides.cherrypickor.domain.Item;

public interface ItemRepository {

    Item getByName(String name);

    Item getByTypeId(String typeId);

}
