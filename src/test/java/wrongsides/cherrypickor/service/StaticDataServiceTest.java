package wrongsides.cherrypickor.service;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wrongsides.cherrypickor.repository.IdRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StaticDataServiceTest {

    private StaticDataService staticDataService;

    @Mock
    private IdRepository idRepository;

    @Before
    public void setUp() {
        staticDataService = new StaticDataService(idRepository);
        when(idRepository.findItemTypeId(any())).thenReturn(Optional.of("1232"));
        when(idRepository.findGroupIds(any())).thenReturn(Lists.newArrayList("345"));
    }

    @Test
    public void loadStaticData_doesStuff() {
        staticDataService.loadStaticData();
    }

}