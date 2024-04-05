package ru.romanov.telerandomizer.definers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.romanov.telerandomizer.entity.PhoneRange;
import ru.romanov.telerandomizer.entity.Region;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RegionDefinerMobile implements RegionDefiner {

    private final RegionDataStorage dataStorage;

    @Override
    public void defineRegion(PhoneRange phoneRange) {
        Optional<Region> region = findRegionByName(phoneRange);
        region.ifPresent(phoneRange::setRegion);
    }

    private Optional<Region> findRegionByName(PhoneRange range) {
        Optional<Region> region = dataStorage.getAllRegions().stream()
                .filter(reg -> range.getLocationFromCsv().contains(reg.getName()))
                .findFirst();

        if (region.isEmpty())
            return findRegionByPartOfName(range);
        else
            return region;
    }

    private Optional<Region> findRegionByPartOfName(PhoneRange range) {
        for (String key : dataStorage.getRegionByPartOfName().keySet()) {
            if (range.getLocationFromCsv().contains(key))
                return Optional.of(dataStorage.getRegionByPartOfName().get(key));
        }
        return Optional.empty();
    }
}