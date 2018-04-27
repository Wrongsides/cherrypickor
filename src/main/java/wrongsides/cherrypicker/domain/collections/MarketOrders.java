package wrongsides.cherrypicker.domain.collections;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wrongsides.cherrypicker.domain.MarketOrder;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketOrders {

    private List<MarketOrder> marketOrders;

    public List<MarketOrder> getMarketOrders() {
        return marketOrders;
    }

    public void setMarketOrders(List<MarketOrder> marketOrders) {
        this.marketOrders = marketOrders;
    }
}
