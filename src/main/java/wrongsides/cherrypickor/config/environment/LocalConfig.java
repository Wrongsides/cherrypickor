package wrongsides.cherrypickor.config.environment;

public class LocalConfig extends Config {

    @Override
    public String getApplicationRoot() {
        return "http://localhost:9000";
    }

    @Override
    public String getEsiUrl() { return "http://localhost:8080"; }
}
