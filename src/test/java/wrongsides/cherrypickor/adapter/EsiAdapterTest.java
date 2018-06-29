package wrongsides.cherrypickor.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.Config;
import wrongsides.cherrypickor.domain.Category;
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
        Item item = new Item(Category.INVENTORY_TYPE, "Bright Spodumain");
        item.getSearchIds().add("17466");
        when(restTemplate.getForObject(anyString(), eq(Item.class))).thenReturn(item);

        Optional<String> itemTypeId = esiAdapter.find("Bright Spodumain", Category.INVENTORY_TYPE, Item.class);

        verify(restTemplate).getForObject("esiUrl/esiVersion/search/?datasource=esiDatasource&categories=inventory_type&search=Bright Spodumain&strict=true", Item.class);
        assertThat(itemTypeId).isEqualTo(Optional.of("17466"));
    }

    @Test
    public void find_givenNonExistantItem_returnsEmpty() {
        when(restTemplate.getForObject(anyString(), eq(Item.class))).thenReturn(new Item(Category.INVENTORY_TYPE, "NonExistantItem"));

        Optional<String> itemTypeId = esiAdapter.find("NonExistantItem", Category.INVENTORY_TYPE, Item.class);

        verify(restTemplate).getForObject("esiUrl/esiVersion/search/?datasource=esiDatasource&categories=inventory_type&search=NonExistantItem&strict=true", Item.class);
        assertThat(itemTypeId).isEmpty();
    }

    @Test
    public void find_givenNullRegion_returnsEmpty() {
        assertThat(esiAdapter.find(null, Category.INVENTORY_TYPE, Item.class)).isEmpty();
    }

    @Test
    public void getByName_givenItemName_returnsDecoratedItem() {
        Item item = new Item();
        item.setSearchIds(Arrays.asList("type-id", "other-type-id"));
        when(restTemplate.getForObject(anyString(), eq(Item.class))).thenReturn(item);

        Item result = esiAdapter.getByName("item-name");

        verify(restTemplate).getForObject("esiUrl/esiVersion/search/?datasource=esiDatasource&categories=inventory_type&search=item-name&strict=true", Item.class);
        assertThat(result.getName()).isEqualTo("item-name");
        assertThat(result.getCategory()).isEqualTo(Category.INVENTORY_TYPE.toString());
        assertThat(result.getTypeId()).isEqualTo("type-id");
    }

    @Test
    public void getByName_givenNullItemName_returnsNullItem() {
        when(restTemplate.getForObject(anyString(), eq(Item.class))).thenReturn(null);
        assertThat(esiAdapter.getByName("null-item-name")).isNull();
    }

    @Test
    public void find_givenIdAndCategory_returnsOptionalItem() {
        Item item = new Item();
        when(restTemplate.getForObject(anyString(), eq(Item.class))).thenReturn(item);

        Optional<Item> result = esiAdapter.find("id", Category.TYPES);

        verify(restTemplate).getForObject("esiUrl/esiVersion/universe/types/id", Item.class);
        assertThat(result.get()).isEqualTo(item);
    }

    @Test
    public void find_givenIdAndCategoryOfNullItem_returnsEmptyOptional() {
        when(restTemplate.getForObject(anyString(), eq(Item.class))).thenReturn(null);
        assertThat(esiAdapter.find("id", Category.TYPES)).isEmpty();
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
    public void find_givenIdAndCategoryOfNullItem_returnsEmptyList() {
        when(restTemplate.getForObject(anyString(), eq(Item.class))).thenReturn(null);
        assertThat(esiAdapter.getCollection("id", Category.CATEGORIES)).isEmpty();
    }
}