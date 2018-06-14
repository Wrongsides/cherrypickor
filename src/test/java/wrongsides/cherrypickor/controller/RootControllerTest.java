package wrongsides.cherrypickor.controller;


import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wrongsides.cherrypickor.controller.resource.NamedResource;
import wrongsides.cherrypickor.service.StaticDataService;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RootControllerTest {

    @Mock
    private StaticDataService staticDataService;

    private RootController rootController;

    @Before
    public void setUp() {
        rootController = new RootController(staticDataService);
    }

    @Test
    public void get_returnsRootResourceWithLinksToSelfAndAsteroids() {

        NamedResource namedResource = rootController.get();

        assertThat(namedResource.getName()).isEqualTo("root");
        assertThat(namedResource.getLinks()).extracting("rel", "href")
                .containsExactly(Tuple.tuple("self", "/"), Tuple.tuple("asteroids", "/asteroids"));
    }
}