package wrongsides.cherrypickor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wrongsides.cherrypickor.controller.resource.NamedResource;
import wrongsides.cherrypickor.service.StaticDataService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/refresh")
public class RefreshController {

    private StaticDataService staticDataService;

    public RefreshController(StaticDataService staticDataService) {
        this.staticDataService = staticDataService;
    }

    @GetMapping
    public NamedResource get() {
        NamedResource namedResource = new NamedResource();
        addLinks(namedResource);
        namedResource.setName("refresh");
        namedResource.setMessage("POST to refresh Asteroid static data");
        return namedResource;
    }

    @PostMapping
    public NamedResource post() {
        NamedResource namedResource = new NamedResource();
        addLinks(namedResource);
        namedResource.setName("refresh");
        namedResource.setMessage(staticDataService.refreshAsteroidStaticData());
        return namedResource;
    }

    private void addLinks(NamedResource resource) {
        resource.add(linkTo(methodOn(RefreshController.class).get()).withSelfRel());
        resource.add(linkTo(methodOn(AsteroidsController.class).get()).withRel("asteroids"));
        resource.add(linkTo(methodOn(RootController.class).get()).withRel("root"));
    }
}
