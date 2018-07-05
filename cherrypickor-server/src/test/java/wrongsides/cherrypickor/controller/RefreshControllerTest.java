package wrongsides.cherrypickor.controller;


import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.hateoas.ResourceSupport;
import wrongsides.cherrypickor.controller.resource.NamedResource;
import wrongsides.cherrypickor.service.StaticDataService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RefreshControllerTest {

    private RefreshController refreshController;

    @Mock
    private StaticDataService staticDataService;

    @Before
    public void setUp() {
        refreshController = new RefreshController(staticDataService);
    }

    @Test
    public void get_returnsNamedResource() {
        NamedResource namedResource = refreshController.get();

        assertThat(namedResource.getName()).isEqualTo("refresh");
        assertThat(namedResource.getMessage()).isEqualTo("POST to refresh Asteroid static data");
        verifyLinks(namedResource);
    }

    @Test
    public void post_returnsNamedResource() {
        when(staticDataService.refreshItemStaticData(anyString())).thenReturn("message");

        NamedResource namedResource = refreshController.post();

        verify(staticDataService).refreshItemStaticData("Dark Ochre");
        assertThat(namedResource.getName()).isEqualTo("refresh");
        assertThat(namedResource.getMessage()).isEqualTo("message");
        verifyLinks(namedResource);
    }

    private void verifyLinks(ResourceSupport resource) {
        assertThat(resource.getLinks()).extracting("rel", "href")
                .containsExactly(Tuple.tuple("self", "/api/refresh"),
                        Tuple.tuple("asteroids", "/api/asteroids"),
                        Tuple.tuple("root", "/api"));
    }
}