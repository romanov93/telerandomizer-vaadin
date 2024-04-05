package ru.romanov.telerandomizer.definers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.romanov.telerandomizer.entity.PhoneRange;
import ru.romanov.telerandomizer.entity.Region;

import java.util.Optional;

import static ru.romanov.telerandomizer.utils.PhoneNumberUtils.getFirstDigitsOfNumber;

@Component
@RequiredArgsConstructor
public class RegionDefinerHome implements RegionDefiner {
    
    private final RegionDataStorage dataStorage;

    @Override
    public void defineRegion(PhoneRange phoneRange) {
        Optional<Region> region = findRegion(phoneRange);
        region.ifPresent(phoneRange::setRegion);
    }

    
    private Optional<Region> findRegion(PhoneRange range) {
        if (range.getCode() == 818)
            return Optional.of(findRegionFor818Code(range));
        else
            return Optional.ofNullable(dataStorage.getRegionByCode().get(range.getCode()));
    }

    /** Код 818 используется сразу для двух регионов, поэтому для него требуется дополнительная проверка на регион: */
    private Region findRegionFor818Code(PhoneRange range) {
        if (getFirstDigitsOfNumber(range.getFirstPhone(), 2) == 53)
            return dataStorage.getAllRegions().get(37);
        else
            return dataStorage.getAllRegions().get(2);
    }
}