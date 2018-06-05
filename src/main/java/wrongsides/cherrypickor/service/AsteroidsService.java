package wrongsides.cherrypickor.service;

import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.domain.*;
import wrongsides.cherrypickor.repository.IdRepository;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class AsteroidsService {

    private IdRepository idRepository;
    private ValuationService valuationService;

    public AsteroidsService(IdRepository idRepository, ValuationService valuationService) {
        this.idRepository = idRepository;
        this.valuationService = valuationService;
    }

    public void sortByValue(List<Asteroid> asteroids) {
        idRepository.findRegionId("The Forge").ifPresent(regionId -> {
            asteroids.forEach((Asteroid asteroid) -> {
                idRepository.findItemTypeId(new Item(Category.INVENTORY_TYPE, asteroid.getName())).ifPresent(asteroidId -> {
                    asteroid.setValue(valuationService.appraise(asteroidId, regionId, asteroid.getQuantity()));
                });
            });
            asteroids.sort((a1, a2) -> a2.getValue().compareTo(a1.getValue()));
        });
    }

    public List<Asteroid> parseScannerOutput(String body) {
        List<Asteroid> asteroids = new ArrayList<>();
        String[] split = body.split("\\n");
        for (String s : split) {
            asteroids.add(stingToAsteroid(s));
        }
        return asteroids;
    }

    private Asteroid stingToAsteroid(String s) {
        Asteroid asteroid = new Asteroid();
        String[] split = s.split("\\t");
        try {
            asteroid.setName(split[0]);
            asteroid.setQuantity(NumberFormat.getNumberInstance(Locale.UK).parse(split[1]).intValue());
            asteroid.setVolume(toMeasure(split[2]));
            asteroid.setDistance(toMeasure(split[3]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return asteroid;
    }

    private Measure toMeasure(String measure) throws ParseException {
        int volumeValue = NumberFormat.getNumberInstance(Locale.UK).parse(measure.substring(0, measure.length() - 3)).intValue();
        Unit volumeUnit = Unit.from(measure.substring(measure.length() - 2, measure.length()));
        return new Measure(volumeValue, volumeUnit);
    }
}
