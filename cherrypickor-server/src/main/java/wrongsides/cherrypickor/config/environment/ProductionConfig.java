package wrongsides.cherrypickor.config.environment;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class ProductionConfig extends Config {

    @Override
    public String getEsiUrl() {
        return "https://esi.evetech.net";
    }
}
