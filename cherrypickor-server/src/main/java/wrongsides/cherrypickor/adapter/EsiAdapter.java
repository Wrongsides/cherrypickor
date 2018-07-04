package wrongsides.cherrypickor.adapter;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.Config;
import wrongsides.cherrypickor.domain.Category;
import wrongsides.cherrypickor.domain.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class EsiAdapter {

    private Config config;
    private RestTemplate restTemplate;

    public EsiAdapter(Config config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public <T extends Search> Optional<String> find(String name, Category category, Class<T> clazz) {
        String url = String.format("%s/%s/search/?datasource=%s&categories=%s&search=%s&strict=true",
                config.getEsiUrl(), config.getEsiVersion(), config.getEsiDatasource(), category.toString(), name);

        T object = restTemplate.getForObject(url, clazz);

        if (object == null || object.getSearchIds().isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(object.getSearchIds().get(0));
        }
    }

    public Item getByName(String name) {
        String url = String.format("%s/%s/search/?datasource=%s&categories=%s&search=%s&strict=true",
                config.getEsiUrl(), config.getEsiVersion(), config.getEsiDatasource(), Category.INVENTORY_TYPE, name);
        Item result = restTemplate.getForObject(url, Item.class);
        if (result != null) {
            result.setName(name);
            result.setCategory(Category.INVENTORY_TYPE.toString());
            result.setTypeId(result.getSearchIds().get(0));
        }
        return result;
    }

    public Optional<Item> find(String id, Category category) {
        String url = String.format("%s/%s/universe/%s/%s",
                config.getEsiUrl(), config.getEsiVersion(), category.toString(), id);
        return Optional.ofNullable(restTemplate.getForObject(url, Item.class));
    }

    public List<String> getCollection(String id, Category category) {
        String url = String.format("%s/%s/universe/%s/%s",
                config.getEsiUrl(), config.getEsiVersion(), category.toString(), id);
        Item object = restTemplate.getForObject(url, Item.class);
        List<String> result = new ArrayList<>();
        if (object != null) {
            if ("categories".equals(category.toString())) {
                result = object.getCategoryGroups();
            } else if ("groups".equals(category.toString())) {
                result = object.getCategoryTypes();
            }
        }
        return result;
    }
}
