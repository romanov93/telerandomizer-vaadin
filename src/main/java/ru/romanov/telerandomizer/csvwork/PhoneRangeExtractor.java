package ru.romanov.telerandomizer.csvwork;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.romanov.telerandomizer.entity.PhoneRange;
import ru.romanov.telerandomizer.definers.CityDefinerHome;
import ru.romanov.telerandomizer.definers.RegionDefinerHome;
import ru.romanov.telerandomizer.definers.RegionDefinerMobile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.romanov.telerandomizer.entity.Phone.NUMBER_TYPE_HOME;
import static ru.romanov.telerandomizer.entity.Phone.NUMBER_TYPE_MOBILE;

@Component
@RequiredArgsConstructor
@Slf4j
public class PhoneRangeExtractor {

    private final RegionDefinerHome regionDefinerHome;
    private final RegionDefinerMobile regionDefinerMobile;
    private final CityDefinerHome cityDefiner;

    public List<PhoneRange> parseRows(List<String> rows) {
        List<PhoneRange> ranges = new ArrayList<>();
        for (String row : rows) {
            PhoneRange range = buildRangeByCsvRow(row);
            if (rangeRefersToTwoRegions(range))
                continue;
            defineRegionAndCity(range);
            Optional.ofNullable(range.getRegion()).ifPresent(region -> ranges.add(range));
        }
        if (ranges.get(0).getNumbersType() == NUMBER_TYPE_HOME) {
            removeRangesWithoutCity(ranges);
            uniteRanges(ranges);
        }
        return ranges.stream().filter(range -> range.getCapacity() > 9).toList();
    }

    private void defineRegionAndCity(PhoneRange range) {
        switch (range.getNumbersType()) {
            case (NUMBER_TYPE_MOBILE) -> {
                regionDefinerMobile.defineRegion(range);
            }
            case (NUMBER_TYPE_HOME) -> {
                regionDefinerHome.defineRegion(range);
                cityDefiner.defineCity(range);
            }
        }
    }

    private PhoneRange buildRangeByCsvRow(String row) {
        String[] cells = row.split(";");
        int code = Integer.parseInt(cells[0]);
        int type = NUMBER_TYPE_HOME;
        if (code >= 900) type = NUMBER_TYPE_MOBILE;
        return PhoneRange.builder()
                .code(code)
                .firstPhone(Integer.parseInt(cells[1]))
                .lastPhone(Integer.parseInt(cells[2]))
                .capacity(Integer.parseInt(cells[3]))
                .locationFromCsv(cells[5])
                .numbersType(type)
                .build();
    }

    private boolean rangeRefersToTwoRegions(PhoneRange range) {
        String info = range.getLocationFromCsv();
        if (range.getNumbersType() == NUMBER_TYPE_MOBILE && info.contains("Ненецкий")) {
            if (info.contains("Архангельск") || info.contains("Ямало"))
                return true;
        }
        return false;
    }

    private void removeRangesWithoutCity(List<PhoneRange> ranges) {
        for (int i = ranges.size() - 1 ; i > 0 ; i--) {
            if (ranges.get(i).getCity() == null)
                ranges.remove(i);
        }
    }

    /** Если диапазоны продолжают друг друга и относятся к одному городу - объединяем их в один */
    private void uniteRanges(List<PhoneRange> ranges) {
        for (int element = ranges.size() - 2 ; element > 0 ; element--) {
            int nextElement = element + 1;
            int lastPhoneInRange = ranges.get(element).getLastPhone();
            int firstPhoneInNextRange = ranges.get(nextElement).getFirstPhone();

            boolean citesOfRangesAreEquals = ranges.get(element).getCity().equals(ranges.get(nextElement).getCity());

            if ( (lastPhoneInRange + 1 == firstPhoneInNextRange) && (citesOfRangesAreEquals) ) {
                ranges.get(element).setLastPhone(ranges.get(nextElement).getLastPhone());
                ranges.get(element).setCapacity(ranges.get(element).getCapacity() + ranges.get(nextElement).getCapacity());
                ranges.remove(nextElement);
            }
        }
    }
}