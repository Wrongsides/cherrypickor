package wrongsides.cherrypickor.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    @JsonAlias("system_id")
    private String systemId;
    @JsonAlias("price")
    private BigDecimal price;
    private ZonedDateTime created;

    public Order() { }

    public Order(String systemId, BigDecimal price) {
        this.systemId = systemId;
        this.price = price;
    }

    public String getSystemId() {
        return systemId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }
}
