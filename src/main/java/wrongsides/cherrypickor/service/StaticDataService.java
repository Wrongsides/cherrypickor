package wrongsides.cherrypickor.service;

import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.domain.Group;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.domain.Type;
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

        String rootAsteroidId = idRepository.findItemTypeId(ROOT_ASTEROID)
                .orElseThrow(RuntimeException::new);

        Type rootAsteroid = getTypeFrom(rootAsteroidId);


        List<Group> groups = getGroups(rootAsteroid);

        //do this async
        groups.forEach(group -> {
            List<Item> itemsFrom = getItemsFrom(group.getTypes());
            //save item
            itemsFrom.forEach(item -> System.out.println(item.toString()));
        });
    }

    private List<Item> getItemsFrom(List<String> typeIds) {
        // post typeIds to /universe/names/
        // map each result to an Item
        Item item = new Item();
        item.setCategory("inventory_type");
        item.setId("1232");
        item.setName("Dark Ochre");
        return Collections.singletonList(item);
    }

    private List<Group> getGroups(Type type) {
        Group group = getGroup(type.getGroupId());
        //get /universe/categories/{category_id}
        return Arrays.asList(group,
                getGroup("450"),
                getGroup("451"),
                getGroup("452"));
    }

    private Group getGroup(String groupId) {
        //get /universe/groups/{group_id}/
        Group group = new Group();
        group.setGroupId(groupId);
        group.setCategoryId("25");
        group.setTypes(Arrays.asList("1232","17436"));
        return group;
    }

    private Type getTypeFrom(String rootAsteroidId) {
        Type type = new Type();
        type.setTypeId(rootAsteroidId);
        type.setGroupId("453");
        return type;
    }
}
