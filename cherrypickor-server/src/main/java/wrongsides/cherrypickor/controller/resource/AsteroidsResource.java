package wrongsides.cherrypickor.controller.resource;

import org.springframework.hateoas.ResourceSupport;
import wrongsides.cherrypickor.domain.Asteroid;

import java.util.ArrayList;
import java.util.List;

public class AsteroidsResource extends ResourceSupport {

    private List<Asteroid> asteroids;

    public AsteroidsResource() {
        this.asteroids = new ArrayList<>();
    }

    public List<Asteroid> getAsteroids() {
        return asteroids;
    }

    public void setAsteroids(List<Asteroid> asteroids) {
        this.asteroids = asteroids;
    }
}
