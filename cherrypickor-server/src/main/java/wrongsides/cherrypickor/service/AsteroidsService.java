package wrongsides.cherrypickor.service;

import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.domain.Asteroid;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.domain.Measure;
import wrongsides.cherrypickor.domain.Unit;
import wrongsides.cherrypickor.repository.IdRepository;
import wrongsides.cherrypickor.repository.ItemRepository;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AsteroidsService {

    private IdRepository idRepository;
    private ItemRepository itemRepository;
    private ValuationService valuationService;

    public AsteroidsService(IdRepository idRepository, ItemRepository itemRepository, ValuationService valuationService) {
        this.idRepository = idRepository;
        this.itemRepository = itemRepository;
        this.valuationService = valuationService;
    }

    public Asteroid getAsteroid(String name) {
        return Asteroid.of(itemRepository.getByName(name).getName()).build();
    }

    public void sortByValue(List<Asteroid> asteroids) {
        String regionId = idRepository.findRegionId("The Forge");
        if (regionId != null) {
            asteroids.forEach((Asteroid asteroid) -> {
                Item item = itemRepository.getByName(asteroid.getName());
                if (item != null) {
                    asteroid.setValue(valuationService.appraise(item.getTypeId(), regionId, asteroid.getQuantity()));
                } else {
                    asteroid.setValue(BigDecimal.ZERO);
                }
            });
            asteroids.sort((a1, a2) -> a2.getValue().compareTo(a1.getValue()));
        }
    }

    public List<Asteroid> parseScannerOutput(String body) {
        String[] split = body.split("\\n");
        return Arrays.stream(split)
                .map(this::stingToAsteroid)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Asteroid stingToAsteroid(String s) {
        Asteroid asteroid = null;
        String[] split = s.split("\\t");
        if (split.length == 4) {
            try {
                asteroid = Asteroid.of(split[0])
                        .withQuantity(NumberFormat.getNumberInstance(Locale.UK).parse(split[1]).intValue())
                        .withVolume(toMeasure(split[2]))
                        .withDistance(toMeasure(split[3]))
                        .build();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return asteroid;
    }

    private Measure toMeasure(String measure) throws ParseException {
        int volumeValue = NumberFormat.getNumberInstance(Locale.UK).parse(measure.substring(0, measure.length() - 3)).intValue();
        Unit volumeUnit = Unit.from(measure.substring(measure.length() - 2, measure.length()));
        return new Measure(volumeValue, volumeUnit);
    }
}
