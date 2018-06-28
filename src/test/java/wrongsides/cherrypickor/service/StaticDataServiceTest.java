package wrongsides.cherrypickor.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.repository.IdRepository;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StaticDataServiceTest {

    private StaticDataService staticDataService;

    @Mock
    private IdRepository idRepository;
    @Mock
    private ItemService itemService;

    @Before
    public void setUp() {
        staticDataService = new StaticDataService(idRepository, itemService);
    }

    @Test
    public void loadStaticData_givenItemsToRefresh_returnsSuccessMessage() {
        when(itemService.removeAll()).thenReturn(true);
        when(idRepository.findTypeId(anyString())).thenReturn("type-id");
        when(idRepository.findGroupId(anyString())).thenReturn("group-id");
        when(idRepository.findCategoryId(anyString())).thenReturn("category-id");
        List<String> groupIds = Arrays.asList("group-id-one", "group-id-two");
        when(idRepository.getGroupIds(anyString())).thenReturn(groupIds);
        List<String> typeIds = Arrays.asList("type-id-one", "type-id-two");
        when(idRepository.getTypeIds(anyString())).thenReturn(typeIds);
        when(itemService.getByTypeId(anyString())).thenReturn(CompletableFuture.completedFuture(new Item()));

        String message = staticDataService.refreshItemStaticData("Dark Ochre");

        InOrder inOrder = inOrder(itemService, idRepository);
        inOrder.verify(itemService).removeAll();
        inOrder.verify(idRepository).findTypeId("Dark Ochre");
        inOrder.verify(idRepository).findGroupId("type-id");
        inOrder.verify(idRepository).findCategoryId("group-id");
        inOrder.verify(idRepository).getGroupIds("category-id");
        inOrder.verify(idRepository).getTypeIds("group-id-one");
        inOrder.verify(idRepository).getTypeIds("group-id-two");
        inOrder.verify(itemService).getByTypeId("type-id-one");
        inOrder.verify(itemService).getByTypeId("type-id-two");
        inOrder.verify(itemService).getByTypeId("type-id-one");
        inOrder.verify(itemService).getByTypeId("type-id-two");
        assertThat(message).isEqualTo("Asteroid item static data refreshed");
    }

    @Test
    public void loadStaticData_givenItemRepositoryIsNotCleared_returnsFailureMessage() {
        when(itemService.removeAll()).thenReturn(false);

        String message = staticDataService.refreshItemStaticData("Dark Ochre");

        verify(itemService).removeAll();
        verifyNoMoreInteractions(itemService);
        verifyZeroInteractions(idRepository);
        assertThat(message).isEqualTo("Failed to clear cache, Asteroid item static data has not been refreshed");
    }
}