package wrongsides.cherrypickor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wrongsides.cherrypickor.controller.resource.AsteroidsResource;
import wrongsides.cherrypickor.domain.Criteria;
import wrongsides.cherrypickor.domain.collections.Asteroids;
import wrongsides.cherrypickor.service.AsteroidsService;

import java.io.IOException;

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

    @PostMapping
    public AsteroidsResource post(@RequestBody String body) {

        Asteroids asteroids = new Asteroids();

        if (body.startsWith("{")){
            try {
                asteroids = objectMapper.readValue(body, Asteroids.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            asteroids.setAsteroids(asteroidsService.parseScannerOutput(body));
        }

        AsteroidsResource asteroidsResource = new AsteroidsResource();

        asteroidsService.sortByValue(asteroids.getAsteroids(), Criteria.VALUE);

        asteroidsResource.setAsteroids(asteroids.getAsteroids());
        asteroidsResource.add(linkTo(methodOn(AsteroidsController.class).post(null)).withSelfRel());
        asteroidsResource.add(linkTo(methodOn(RootController.class).get()).withRel("root"));
        return asteroidsResource;
    }
}
