package wrongsides.cherrypickor.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wrongsides.cherrypickor.adapter.EsiAdapter;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.domain.Region;

import static org.mockito.Mockito.verify;

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
    public void findItemTypeId_givenBrightSpodumain_delegatesToEsiAdapter() {

        idRepository.findItemTypeId("Bright Spodumain");

        verify(esiAdapter).find("Bright Spodumain", "inventory_type", Item.class);
    }

    @Test
    public void findRegion_givenTheForge_delegatesToEsiAdapte() {

        idRepository.findRegion("The Forge");

        verify(esiAdapter).find("The Forge", "region", Region.class);
    }
}