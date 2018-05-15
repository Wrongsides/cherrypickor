package wrongsides.cherrypickor.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.Config;
import wrongsides.cherrypickor.domain.InventoryType;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EsiAdapterTest {

    @Mock
    private Config config;
    @Mock
    private RestTemplate restTemplate;

    private EsiAdapter esiAdapter;

    @Before
    public void setUp() {
        esiAdapter = new EsiAdapter(config, restTemplate);
        when(config.getEsiUrl()).thenReturn("esiUrl");
        when(config.getEsiVersion()).thenReturn("esiVersion");
        when(config.getEsiDatasource()).thenReturn("esiDatasource");
    }

    @Test
    public void find_givenBrightSpodumain_returnsOptionalOf17466() {
        InventoryType inventoryType = new InventoryType();
        inventoryType.getIds().add("17466");
        when(restTemplate.getForObject(anyString(), eq(InventoryType.class))).thenReturn(inventoryType);

        Optional<String> itemTypeId = esiAdapter.find("Bright Spodumain", "inventory_type", InventoryType.class);

        verify(restTemplate).getForObject("esiUrl/esiVersion/search/?datasource=esiDatasource&categories=inventory_type&search=Bright Spodumain&strict=true", InventoryType.class);
        assertThat(itemTypeId).isEqualTo(Optional.of("17466"));
    }

    @Test
    public void find_givenNonExistantItem_returnsEmpty() {
        when(restTemplate.getForObject(anyString(), eq(InventoryType.class))).thenReturn(new InventoryType());

        Optional<String> itemTypeId = esiAdapter.find("NonExistantItem", "inventory_type", InventoryType.class);

        verify(restTemplate).getForObject("esiUrl/esiVersion/search/?datasource=esiDatasource&categories=inventory_type&search=NonExistantItem&strict=true", InventoryType.class);
        assertThat(itemTypeId).isEmpty();
    }

    @Test
    public void find_givenNullRegion_returnsEmpty() {
        assertThat(esiAdapter.find(null, "inventory_type", InventoryType.class)).isEmpty();
    }
}