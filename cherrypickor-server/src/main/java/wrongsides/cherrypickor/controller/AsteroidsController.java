package wrongsides.cherrypickor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.*;
import wrongsides.cherrypickor.controller.resource.AsteroidsResource;
import wrongsides.cherrypickor.controller.resource.NamedResource;
import wrongsides.cherrypickor.service.AsteroidsService;

import java.io.IOException;
import java.text.ParseException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/api/asteroids")
public class AsteroidsController {

    private AsteroidsService asteroidsService;
    private ObjectMapper objectMapper;

    public AsteroidsController(AsteroidsService asteroidsService, ObjectMapper objectMapper) {
        this.asteroidsService = asteroidsService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public NamedResource get() {
        NamedResource namedResource = new NamedResource();
        addLinks(namedResource);
        namedResource.setName("asteroids");
        namedResource.setMessage("POST scanner output or JSON asteroids for appraisal");
        return namedResource;
    }

    @PostMapping
    public AsteroidsResource post(@RequestBody String body) throws IOException, ParseException {
        AsteroidsResource asteroidsResource = new AsteroidsResource();
        if (body != null) {
            if (body.startsWith("{")) {
                asteroidsResource = objectMapper.readValue(body, AsteroidsResource.class);
            } else {
                asteroidsResource.setAsteroids(asteroidsService.parseScannerOutput(body));
            }
            asteroidsService.sortByValue(asteroidsResource.getAsteroids());
        }
        addLinks(asteroidsResource);
        return asteroidsResource;
    }

    private void addLinks(ResourceSupport resource) {
        resource.add(linkTo(methodOn(AsteroidsController.class).get()).withSelfRel());
        resource.add(linkTo(methodOn(RefreshController.class).get()).withRel("refresh"));
        resource.add(linkTo(methodOn(RootController.class).get()).withRel("root"));
    }
}
