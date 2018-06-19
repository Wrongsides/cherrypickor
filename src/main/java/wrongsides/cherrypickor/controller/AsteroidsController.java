package wrongsides.cherrypickor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wrongsides.cherrypickor.controller.resource.AsteroidsResource;
import wrongsides.cherrypickor.domain.collections.Asteroids;
import wrongsides.cherrypickor.service.AsteroidsService;

import java.io.IOException;
import java.util.Collections;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/asteroids")
public class AsteroidsController {

    private AsteroidsService asteroidsService;
    private ObjectMapper objectMapper;

    @Autowired
    public AsteroidsController(AsteroidsService asteroidsService, ObjectMapper objectMapper) {
        this.asteroidsService = asteroidsService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public AsteroidsResource get(@RequestParam String name) {
        AsteroidsResource asteroidsResource = new AsteroidsResource();
        asteroidsResource.add(linkTo(methodOn(AsteroidsController.class).get(null)).withSelfRel());
        asteroidsResource.add(linkTo(methodOn(RootController.class).get()).withRel("root"));
        asteroidsResource.setAsteroids(Collections.singletonList(asteroidsService.getAsteroid(name)));
        return asteroidsResource;
    }

    @PostMapping
    public AsteroidsResource post(@RequestBody String body) {
        AsteroidsResource asteroidsResource = new AsteroidsResource();
        asteroidsResource.add(linkTo(methodOn(AsteroidsController.class).post(null)).withSelfRel());
        asteroidsResource.add(linkTo(methodOn(RootController.class).get()).withRel("root"));

        if (body == null) {
            return asteroidsResource;
        }

        Asteroids asteroids = new Asteroids();

        if (body.startsWith("{")) {
            try {
                asteroids = objectMapper.readValue(body, Asteroids.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            asteroids.setAsteroids(asteroidsService.parseScannerOutput(body));
        }

        asteroidsService.sortByValue(asteroids.getAsteroids());

        asteroidsResource.setAsteroids(asteroids.getAsteroids());
        return asteroidsResource;
    }
}
