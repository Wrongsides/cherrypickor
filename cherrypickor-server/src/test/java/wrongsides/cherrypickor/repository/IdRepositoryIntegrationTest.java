package wrongsides.cherrypickor.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import wrongsides.cherrypickor.adapter.EsiAdapter;
import wrongsides.cherrypickor.config.CacheConfig;
import wrongsides.cherrypickor.domain.Category;
import wrongsides.cherrypickor.domain.Region;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CacheConfig.class, IdRepository.class})
public class IdRepositoryIntegrationTest {

    @Autowired
    private IdRepository idRepository;

    @MockBean
    private EsiAdapter esiAdapter;

    @Test
    public void findRegionId_cachesRequestWithKeyOfRegionName() {
        when(esiAdapter.find(anyString(), any(Category.class), eq(Region.class))).thenReturn(Optional.of("region-id"));

        String regionId = idRepository.findRegionId("region-name");
        String cachedRegionId = idRepository.findRegionId("region-name");

        verify(esiAdapter, times(1)).find("region-name", Category.REGION, Region.class);
        verifyNoMoreInteractions(esiAdapter);
        assertThat(regionId).isEqualTo("region-id");
        assertThat(cachedRegionId).isEqualTo("region-id");
    }
}