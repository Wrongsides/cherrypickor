package wrongsides.cherrypickor.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import wrongsides.cherrypickor.config.AsyncConfig;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.repository.ItemRepository;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AsyncConfig.class, ItemService.class})
public class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;

    @Test
    public void getByTypeId_callsItemRepositoryAsynchronously() {
        await().atMost(500L, TimeUnit.MILLISECONDS)
                .until(() -> {
                    CountDownLatch countDownLatch = new CountDownLatch(1);
                    when(itemRepository.getByTypeId(anyString())).thenAnswer(invocation -> {
                        countDownLatch.await();
                        return Mockito.mock(Item.class);
                    }).thenAnswer(invocation -> {
                        countDownLatch.countDown();
                        return Mockito.mock(Item.class);
                    });
                    CompletableFuture<Item> one = itemService.getByTypeId("one");
                    CompletableFuture<Item> two = itemService.getByTypeId("two");
                    CompletableFuture.allOf(one, two).join();
                    return true;
                });
    }
}