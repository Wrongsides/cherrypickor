package wrongsides.cherrypickor.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.Config;
import wrongsides.cherrypickor.domain.Region;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
    }

    @Test
    public void findRegion_givenTheForge_returnsOptionalOf10000002() {
        Region region = new Region();
        region.getRegionIds().add("10000002");
        when(restTemplate.getForObject(anyString(), eq(Region.class))).thenReturn(region);

        Optional<String> regionId = idRepository.findRegion("The Forge");

        assertThat(regionId.get()).isEqualTo("10000002");
    }
}