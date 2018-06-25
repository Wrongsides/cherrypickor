package wrongsides.cherrypickor.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import wrongsides.cherrypickor.adapter.EsiAdapter;
import wrongsides.cherrypickor.config.AsyncConfig;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.repository.SimpleItemRepository;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        AsyncConfig.class,
        ItemService.class,
        SimpleItemRepository.class
})
public class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @MockBean
    private EsiAdapter esiAdapter;

    @Test
    public void getByTypeId_callsItemRepositoryAsynchronously() {
        await().atMost(500L, TimeUnit.MILLISECONDS)
                .until(() -> {
                    when(esiAdapter.find(anyString())).thenAnswer(invocation -> {
                        Thread.sleep(200L);
                        return Mockito.mock(Item.class);
                    });
                    CompletableFuture<Item> one = itemService.getByTypeId("one");
                    CompletableFuture<Item> two = itemService.getByTypeId("two");
                    CompletableFuture<Item> three = itemService.getByTypeId("three");
                    CompletableFuture.allOf(one, two, three).join();
                    return true;
                });
    }
}