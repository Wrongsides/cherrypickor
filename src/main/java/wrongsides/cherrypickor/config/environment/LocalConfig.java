package wrongsides.cherrypickor.config.environment;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class LocalConfig extends Config {

    @Override
    public String getEsiUrl() { return "http://localhost:8080"; }
}
