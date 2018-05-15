package wrongsides.cherrypickor.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.Config;
import wrongsides.cherrypickor.domain.InventoryType;
import wrongsides.cherrypickor.domain.Region;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IdRepositoryTest {

    @Mock
    private Config config;
    @Mock
    private RestTemplate restTemplate;

    private IdRepository idRepository;

    @Before
    public void setUp() {
        idRepository = new IdRepository(config, restTemplate);
        when(config.getEsiUrl()).thenReturn("esiUrl");
        when(config.getEsiVersion()).thenReturn("esiVersion");
        when(config.getEsiDatasource()).thenReturn("esiDatasource");
    }

    @Test
    public void findItemTypeId_givenBrightSpodumain_returnsOptionalOf17466() {
        InventoryType inventoryType = new InventoryType();
        inventoryType.getInventoryTypeIds().add("17466");
        when(restTemplate.getForObject(anyString(), eq(InventoryType.class))).thenReturn(inventoryType);

        Optional<String> itemTypeId = idRepository.findItemTypeId("Bright Spodumain");

        verify(restTemplate).getForObject("esiUrl/esiVersion/search/?datasource=esiDatasource&categories=inventory_type&search=Bright Spodumain&strict=true", InventoryType.class);
        assertThat(itemTypeId).isEqualTo(Optional.of("17466"));
    }

    @Test
    public void findItemTypeId_givenNonExistantItem_returnsEmpty() {
        when(restTemplate.getForObject(anyString(), eq(InventoryType.class))).thenReturn(new InventoryType());

        Optional<String> itemTypeId = idRepository.findItemTypeId("NonExistantItem");

        verify(restTemplate).getForObject("esiUrl/esiVersion/search/?datasource=esiDatasource&categories=inventory_type&search=NonExistantItem&strict=true", InventoryType.class);
        assertThat(itemTypeId).isEmpty();
    }

    @Test
    public void findItemTypeId_givenNullRegion_returnsEmpty() {
        assertThat(idRepository.findItemTypeId(null)).isEmpty();
    }

    @Test
    public void findRegion_givenTheForge_returnsOptionalOf10000002() {
        Region region = new Region();
        region.getRegionIds().add("10000002");
        when(restTemplate.getForObject(anyString(), eq(Region.class))).thenReturn(region);

        Optional<String> regionId = idRepository.findRegion("The Forge");

        verify(restTemplate).getForObject("esiUrl/esiVersion/search/?datasource=esiDatasource&categories=region&search=The Forge&strict=true", Region.class);
        assertThat(regionId).isEqualTo(Optional.of("10000002"));
    }

    @Test
    public void findRegion_givenNonExistantRegion_returnsEmpty() {
        when(restTemplate.getForObject(anyString(), eq(Region.class))).thenReturn(new Region());

        Optional<String> regionId = idRepository.findRegion("NonExistantRegion");

        verify(restTemplate).getForObject("esiUrl/esiVersion/search/?datasource=esiDatasource&categories=region&search=NonExistantRegion&strict=true", Region.class);
        assertThat(regionId).isEmpty();
    }

    @Test
    public void findRegion_givenNullRegion_returnsEmpty() {
        assertThat(idRepository.findRegion(null)).isEmpty();
    }
}