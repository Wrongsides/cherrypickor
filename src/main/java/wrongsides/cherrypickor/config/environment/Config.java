package wrongsides.cherrypickor.config.environment;


public abstract class Config {

    public String getEsiUrl() {
        return "https://esi.tech.ccp.is";
    }

    public String getEsiVersion() {
        return "latest";
    }

    public String getEsiDatasource() {
        return "tranquility";
    }

    public String getApplicationRoot() {
        return "";
    }
}
