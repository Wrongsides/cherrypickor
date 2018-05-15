package wrongsides.cherrypickor.config.environment;

public class LocalConfig extends Config {

    @Override
    public String getEsiUrl() { return "http://localhost:8080"; }
}
