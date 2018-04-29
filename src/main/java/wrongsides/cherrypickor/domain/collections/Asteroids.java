package wrongsides.cherrypickor.domain.collections;

import wrongsides.cherrypickor.domain.Asteroid;

import java.util.ArrayList;
import java.util.List;

public class Asteroids {

    private List<Asteroid> asteroids;

    public List<Asteroid> getAsteroids() {
        if (asteroids == null) {
            asteroids = new ArrayList<>();
        }
        return asteroids;
    }

    public void setAsteroids(List<Asteroid> asteroids) {
        this.asteroids = asteroids;
    }
}
