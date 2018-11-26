package wrongsides.cherrypickor.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.Config;
import wrongsides.cherrypickor.domain.Item;

import java.util.Arrays;
import java.util.List;
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
    @Mock
    private ResponseEntity<String> responseEntity;

    private ObjectMapper objectMapper;
    private EsiAdapter esiAdapter;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        esiAdapter = new EsiAdapter(config, restTemplate, objectMapper);
        when(config.getEsiUrl()).thenReturn("esiUrl");
        when(config.getEsiVersion()).thenReturn("esiVersion");
        when(config.getEsiDatasource()).thenReturn("esiDatasource");
    }

    @Test
    public void find_givenBrightSpodumain_returnsOptionalOf17466() {
        String jsonString = "{\"inventory_type\":[\"17466\"]}";
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(jsonString);

        Optional<String> itemTypeId = esiAdapter.find("Bright Spodumain", Category.INVENTORY_TYPE);

        verify(restTemplate).getForEntity("esiUrl/esiVersion/search/?datasource=esiDatasource&categories=inventory_type&search=Bright Spodumain&strict=true", String.class);
        assertThat(itemTypeId).isEqualTo(Optional.of("17466"));
    }

    @Test
    public void find_givenNonArrayItem_returnsEmpty() {
        String jsonString = "{\"inventory_type\":\"hello\"}";
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(jsonString);

        Optional<String> itemTypeId = esiAdapter.find("NonArrayItem", Category.INVENTORY_TYPE);

        verify(restTemplate).getForEntity("esiUrl/esiVersion/search/?datasource=esiDatasource&categories=inventory_type&search=NonArrayItem&strict=true", String.class);
        assertThat(itemTypeId).isEqualTo(Optional.empty());
    }

    @Test
    public void find_givenNonExistantItem_returnsEmpty() {
        String jsonString = "{\"inventory_type\":[]}";
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(jsonString);

        Optional<String> itemTypeId = esiAdapter.find("NonExistantItem", Category.INVENTORY_TYPE);

        verify(restTemplate).getForEntity("esiUrl/esiVersion/search/?datasource=esiDatasource&categories=inventory_type&search=NonExistantItem&strict=true", String.class);
        assertThat(itemTypeId).isEmpty();
    }

    @Test
    public void find_givenIOException_returnsEmpty() {
        String jsonString = null;
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(jsonString);

        Optional<String> itemTypeId = esiAdapter.find("Boom", Category.INVENTORY_TYPE);

        verify(restTemplate).getForEntity("esiUrl/esiVersion/search/?datasource=esiDatasource&categories=inventory_type&search=Boom&strict=true", String.class);
        assertThat(itemTypeId).isEmpty();
    }

    @Test
    public void find_givenNullRegion_returnsEmpty() {
        assertThat(esiAdapter.find(null, Category.INVENTORY_TYPE)).isEmpty();
    }

    @Test
    public void findItem_givenIdAndCategory_returnsOptionalItem() {
        Item item = new Item();
        when(restTemplate.getForObject(anyString(), eq(Item.class))).thenReturn(item);

        Optional<Item> result = esiAdapter.findItem("id", Category.TYPES);

        verify(restTemplate).getForObject("esiUrl/esiVersion/universe/types/id", Item.class);
        assertThat(result.get()).isEqualTo(item);
    }

    @Test
    public void findItem_givenIdAndCategoryOfNullItem_returnsEmptyOptional() {
        when(restTemplate.getForObject(anyString(), eq(Item.class))).thenReturn(null);
        assertThat(esiAdapter.findItem("id", Category.TYPES)).isEmpty();
    }

    @Test
    public void getCollection_givenIdAndCategoryGroups_returnsTypes() {
        Item item = new Item();
        List<String> types = Arrays.asList("type-id", "other-type-id");
        item.setCategoryTypes(types);
        when(restTemplate.getForObject(anyString(), eq(Item.class))).thenReturn(item);

        List<String> result = esiAdapter.getCollection("id", Category.GROUPS);

        verify(restTemplate).getForObject("esiUrl/esiVersion/universe/groups/id", Item.class);
        assertThat(result).isEqualTo(types);
    }

    @Test
    public void getCollection_givenIdAndCategoryCategories_returnsGroups() {
        Item item = new Item();
        List<String> groups = Arrays.asList("group-id", "other-group-id");
        item.setCategoryGroups(groups);
        when(restTemplate.getForObject(anyString(), eq(Item.class))).thenReturn(item);

        List<String> result = esiAdapter.getCollection("id", Category.CATEGORIES);

        verify(restTemplate).getForObject("esiUrl/esiVersion/universe/categories/id", Item.class);
        assertThat(result).isEqualTo(groups);
    }

    @Test
    public void getCollection_givenIdAndOtherCategory_returnsEmptyList() {
        Item item = new Item();
        when(restTemplate.getForObject(anyString(), eq(Item.class))).thenReturn(item);

        List<String> result = esiAdapter.getCollection("id", Category.INVENTORY_TYPE);

        verify(restTemplate).getForObject("esiUrl/esiVersion/universe/inventory_type/id", Item.class);
        assertThat(result).isEmpty();
    }

    @Test
    public void getCollection_givenIdAndCategoryOfNullItem_returnsEmptyList() {
        when(restTemplate.getForObject(anyString(), eq(Item.class))).thenReturn(null);
        assertThat(esiAdapter.getCollection("id", Category.CATEGORIES)).isEmpty();
    }
}