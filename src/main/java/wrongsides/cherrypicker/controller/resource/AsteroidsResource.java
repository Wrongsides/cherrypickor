package wrongsides.cherrypicker.controller.resource;

import org.springframework.hateoas.ResourceSupport;
import wrongsides.cherrypicker.domain.Asteroid;
import java.util.List;

public class AsteroidsResource extends ResourceSupport {

    private List<Asteroid> asteroids;

    public List<Asteroid> getAsteroids() {
        return asteroids;
    }

    public void setAsteroids(List<Asteroid> asteroids) {
        this.asteroids = asteroids;
    }
}
