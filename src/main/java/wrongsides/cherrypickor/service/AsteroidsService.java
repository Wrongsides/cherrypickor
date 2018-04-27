package wrongsides.cherrypickor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.domain.Asteroid;
import wrongsides.cherrypickor.domain.Criteria;
import wrongsides.cherrypickor.domain.Measure;
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

    @Autowired
    public AsteroidsService(IdRepository idRepository, ValuationService valuationService) {
        this.idRepository = idRepository;
        this.valuationService = valuationService;
    }

    public void sortByValue(List<Asteroid> asteroids, Criteria valuationCriteria) {

        String regionId = idRepository.findRegion("The Forge");

        asteroids.forEach((Asteroid asteroid) -> {
            String asteroidId = idRepository.findItemTypeId(asteroid.getName());
            asteroid.setValue(valuationService.appraise(asteroidId, regionId, asteroid.getQuantity(), valuationCriteria));
        });

        asteroids.sort((a1, a2) -> a2.getValue().compareTo(a1.getValue()));
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

        //Bright Spodumain	13,396	214,336 m3	188 km

        try {
            asteroid.setName(split[0]);
            asteroid.setQuantity(NumberFormat.getNumberInstance(Locale.UK).parse(split[1]).intValue());



            String volumeStr = split[2];

            Measure volume = new Measure();
            volume.setValue(NumberFormat.getNumberInstance(Locale.UK).parse(volumeStr.substring(0, volumeStr.length()-3)).intValue());
            volume.setUnit(volumeStr.substring(volumeStr.length()-2, volumeStr.length()));
            asteroid.setVolume(volume);

            String distanceStr = split[3];
            Measure distance = new Measure();
            distance.setValue(NumberFormat.getNumberInstance(Locale.UK).parse(distanceStr.substring(0, distanceStr.length()-3)).intValue());
            distance.setUnit(distanceStr.substring(distanceStr.length()-2, distanceStr.length()));
            asteroid.setDistance(distance);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return asteroid;
    }
}
