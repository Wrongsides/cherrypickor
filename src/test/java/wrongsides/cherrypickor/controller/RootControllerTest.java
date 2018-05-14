package wrongsides.cherrypickor.controller;


import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import wrongsides.cherrypickor.controller.resource.NamedResource;

import static org.assertj.core.api.Assertions.assertThat;

public class RootControllerTest {

    private RootController rootController;

    @Before
    public void setUp() {
        rootController = new RootController();
    }

    @Test
    public void get_returnsRootResourceWithLinksToSelfAndAsteroids() {

        NamedResource namedResource = rootController.get();

        assertThat(namedResource.getName()).isEqualTo("root");
        assertThat(namedResource.getLinks()).extracting("rel", "href")
                .containsExactly(Tuple.tuple("self", "/"), Tuple.tuple("asteroids", "/asteroids"));
    }
}