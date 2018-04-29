package wrongsides.cherrypickor.config.environment;

public class LocalConfig extends Config {

    @Override
    public String getApplicationRoot() {
        return "http://localhost:8080";
    }
}
