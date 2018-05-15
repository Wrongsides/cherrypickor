package wrongsides.cherrypickor.config.environment;

public class ProductionConfig extends Config {

    @Override
    public String getEsiUrl() { return "https://esi.tech.ccp.is"; }
}
