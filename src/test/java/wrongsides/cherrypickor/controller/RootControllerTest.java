package wrongsides.cherrypickor.controller;


import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import wrongsides.cherrypickor.controller.resource.NamedResource;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RootControllerTest {

    private RootController rootController = new RootController();

    @Test
    public void get_returnsRootResourceWithLinks() {

        NamedResource namedResource = rootController.get();

        assertThat(namedResource.getName()).isEqualTo("root");
        assertThat(namedResource.getMessage()).isEqualTo("Welcome to Cherrypickor!");
        assertThat(namedResource.getLinks()).extracting("rel", "href")
                .containsExactly(Tuple.tuple("self", "/"),
                        Tuple.tuple("asteroids", "/asteroids"),
                        Tuple.tuple("refresh", "/refresh"));
    }
}