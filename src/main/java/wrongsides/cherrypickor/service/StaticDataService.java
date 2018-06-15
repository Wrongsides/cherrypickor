package wrongsides.cherrypickor.service;

import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.domain.Category;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.domain.ItemSummary;
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
        idRepository.setGroupId(rootAsteroid);
        idRepository.setCategoryId(rootAsteroid);

        idRepository.setGroupIds(rootAsteroid.getCategoryId()).forEach(groupId -> {
            List<String> typeIds = idRepository.setTypeIds(groupId);
            if(!typeIds.isEmpty()){
                List<ItemSummary> itemsFrom = idRepository.getItemSummaries(typeIds);
                //save item
                itemsFrom.forEach(item -> System.out.println(item.toString()));
            }
        });
    }
}
