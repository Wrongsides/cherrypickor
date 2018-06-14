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
@RequestMapping(path = "/")
public class RootController {

    private StaticDataService staticDataService;

    public RootController(StaticDataService staticDataService) {
        this.staticDataService = staticDataService;
    }

    @GetMapping
    public NamedResource get(){
        NamedResource namedResource = new NamedResource();
        namedResource.setName("root");
        namedResource.add(linkTo(methodOn(RootController.class).get()).withSelfRel());
        namedResource.add(linkTo(methodOn(AsteroidsController.class).post(null)).withRel("asteroids"));
        return namedResource;
    }


    @PostMapping
    public String post(){
        staticDataService.loadStaticData();
        return "loading static data...";
    }
}
