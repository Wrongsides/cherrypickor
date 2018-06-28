package wrongsides.cherrypickor.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wrongsides.cherrypickor.adapter.EsiAdapter;
import wrongsides.cherrypickor.domain.Category;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.domain.Region;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IdRepositoryTest {

    private IdRepository idRepository;

    @Mock
    private EsiAdapter esiAdapter;

    @Before
    public void setUp() {
        idRepository = new IdRepository(esiAdapter);
    }

    @Test
    public void findRegion_givenRegionName_delegatesToEsiAdapter() {
        idRepository.findRegionId("The Forge");
        verify(esiAdapter).find("The Forge", Category.REGION, Region.class);
    }

    @Test
    public void findRegion_givenUnknownRegionName_returnsNull() {
        when(esiAdapter.find(anyString(), any(Category.class), any())).thenReturn(Optional.empty());

        String result = idRepository.findRegionId("Unknown");

        verify(esiAdapter).find("Unknown", Category.REGION, Region.class);
        assertThat(result).isNull();
    }

    @Test
    public void findItemTypeId_givenTypeName_delegatesToEsiAdapter() {
        idRepository.findTypeId("Bright Spodumain");
        verify(esiAdapter).find("Bright Spodumain", Category.INVENTORY_TYPE, Item.class);
    }

    @Test
    public void findItemTypeId_givenUnknownTypeName_returnsNull() {
        when(esiAdapter.find(anyString(), any(Category.class), any())).thenReturn(Optional.empty());

        String result = idRepository.findTypeId("Unknown");

        verify(esiAdapter).find("Unknown", Category.INVENTORY_TYPE, Item.class);
        assertThat(result).isNull();
    }

    @Test
    public void findGroupId_givenTypeId_delegatesToEsiAdapter() {
        idRepository.findGroupId("type-id");
        verify(esiAdapter).find("type-id", Category.TYPES);
    }

    @Test
    public void findGroupId_givenUnknownTypeId_returnsNull() {
        when(esiAdapter.find(anyString(), any(Category.class))).thenReturn(Optional.empty());

        String result = idRepository.findGroupId("Unknown");

        verify(esiAdapter).find("Unknown", Category.TYPES);
        assertThat(result).isNull();
    }

    @Test
    public void findCategoryId_givenGroupId_delegatesToEsiAdapter() {
        idRepository.findCategoryId("group-id");
        verify(esiAdapter).find("group-id", Category.GROUPS);
    }

    @Test
    public void findCategoryId_givenUnknownGroupId_returnsNull() {
        when(esiAdapter.find(anyString(), any(Category.class))).thenReturn(Optional.empty());

        String result = idRepository.findCategoryId("Unknown");

        verify(esiAdapter).find("Unknown", Category.GROUPS);
        assertThat(result).isNull();
    }

    @Test
    public void getGroupIds_givenCategoryId_delegatesToEsiAdapter() {
        idRepository.getGroupIds("category-id");
        verify(esiAdapter).getCollection("category-id", Category.CATEGORIES);
    }

    @Test
    public void getTypeIds_givenCategoryId_delegatesToEsiAdapter() {
        idRepository.getTypeIds("group-id");
        verify(esiAdapter).getCollection("group-id", Category.GROUPS);
    }
}