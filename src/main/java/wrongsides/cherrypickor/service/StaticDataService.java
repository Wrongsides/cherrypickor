package wrongsides.cherrypickor.service;

import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.domain.Category;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.repository.IdRepository;

import java.util.*;

@Service
public class StaticDataService {

    private static final String ROOT_ASTEROID = "Dark Ochre";
    private IdRepository idRepository;

    public StaticDataService(IdRepository idRepository) {
        this.idRepository = idRepository;
    }

    public void loadStaticData() {
        Item rootAsteroid = new Item(Category.INVENTORY_TYPE, ROOT_ASTEROID);
        rootAsteroid.setTypeId(idRepository.findItemTypeId(rootAsteroid).orElse(""));
        rootAsteroid.setGroupId(idRepository.findGroupId(rootAsteroid).orElse(""));
        rootAsteroid.setCategory(idRepository.findCategoryId(rootAsteroid).orElse(""));

        idRepository.findGroupIds(rootAsteroid.getCategoryId()).forEach(groupId -> {
            List<String> typeIds = idRepository.findTypeIds(groupId);
            List<Item> itemsFrom = getItemsFrom(typeIds);
            //save item
            itemsFrom.forEach(item -> System.out.println(item.toString()));
        });
    }

    private List<Item> getItemsFrom(List<String> typeIds) {
        // post typeIds to /universe/names/
        // map each result to an Item
        Item item = new Item(Category.INVENTORY_TYPE, ROOT_ASTEROID);
        item.setCategory("inventory_type");
        item.setTypeId("1232");
        item.setName("Dark Ochre");
        return Collections.singletonList(item);
    }
}
