package wrongsides.cherrypickor.adapter;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.Config;

import java.util.Optional;

@Component
public class EsiAdapter {

    private Config config;
    private RestTemplate restTemplate;

    public EsiAdapter(Config config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public <T extends Search> Optional<String> find(String name, String category, Class<T> clazz) {
        String url = String.format("%s/%s/search/?datasource=%s&categories=%s&search=%s&strict=true",
                config.getEsiUrl(), config.getEsiVersion(), config.getEsiDatasource(), category, name);

        T object = restTemplate.getForObject(url, clazz);

        if (object == null || object.getIds().isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(object.getIds().get(0));
        }
    }
}