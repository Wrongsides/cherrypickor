package wrongsides.cherrypickor.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.Config;
import wrongsides.cherrypickor.domain.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class EsiAdapter {

    private Config config;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    public EsiAdapter(Config config, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.config = config;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Optional<String> find(String name, Category category) {
        String url = String.format("%s/%s/search/?datasource=%s&categories=%s&search=%s&strict=true",
                config.getEsiUrl(), config.getEsiVersion(), config.getEsiDatasource(), category.toString(), name);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        try {
            JsonNode search = objectMapper.readTree(response.getBody()).path(category.toString());
            if (search.isArray() && search.hasNonNull(0)) {
                return Optional.of(search.get(0).asText());
            }
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Item> findItem(String id, Category category) {
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
