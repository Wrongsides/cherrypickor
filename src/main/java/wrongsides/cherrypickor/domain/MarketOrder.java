package wrongsides.cherrypickor.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketOrder {

    public MarketOrder() { }

    public MarketOrder(String systemId, BigDecimal price) {
        this.systemId = systemId;
        this.price = price;
    }

    @JsonAlias("system_id")
    private String systemId;

    @JsonAlias("price")
    private BigDecimal price;

    public String getSystemId() {
        return systemId;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
