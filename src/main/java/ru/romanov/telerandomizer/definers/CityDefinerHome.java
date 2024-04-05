package ru.romanov.telerandomizer.definers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.romanov.telerandomizer.entity.City;
import ru.romanov.telerandomizer.entity.PhoneRange;
import ru.romanov.telerandomizer.entity.Region;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static ru.romanov.telerandomizer.utils.PhoneNumberUtils.getFirstDigitsOfNumber;

@Component
@RequiredArgsConstructor
public class CityDefinerHome implements CityDefiner {

    private final RegionDataStorage dataStorage;

    @Override
    public void defineCity(PhoneRange phoneRange) {
        Optional<City> city = findCity(phoneRange);
        city.ifPresent(phoneRange::setCity);
    }

    private Optional<City> findCity(PhoneRange range) {
        if(range.getCode() == 495)
            return findCityFor495RegionCode(range);
        else if (range.getCode() == 814)
            return findCityFor814RegionCode(range);
        else
            return findCityByCode(range);
    }

    private Optional<City> findCityByCode(PhoneRange range) {
        Map<Integer, City> cityByCode = dataStorage.getCityByCodeByRegionId().get(range.getRegion().getId());

        City city = null;
        int codeLength = 5;
        while (range.getCity() == null && codeLength != 0) {
            int cityCode = getFirstDigitsOfNumber(range.getFirstPhone(), codeLength);
            city = cityByCode.get(cityCode);
            codeLength --;
        }
        return Optional.ofNullable(city);
    }

    /** В некоторых регионах трудно определить город по коду, в таких случаях используем инфу из csv-файлов */
    private Optional<City> findCityFor495RegionCode(PhoneRange range) {
        Region Moscow495 = dataStorage.getAllRegions().get(32);
        Set<City> cities495 = dataStorage.getCitiesByRegionId().get(Moscow495.getId());

        if (range.getLocationFromCsv().contains("Королёв"))
            return cities495.stream()
                    .filter(city -> city.getName().equals("г. Королев"))
                    .findFirst();
        else
            return cities495.stream()
                .filter(city -> range.getLocationFromCsv().contains(city.getName()))
                .findFirst();
    }

    private Optional<City> findCityFor814RegionCode(PhoneRange range) {
        Region Karelia = dataStorage.getAllRegions().get(55);
        Set<City> cities814 = dataStorage.getCitiesByRegionId().get(Karelia.getId());

        return cities814.stream()
                .filter(city -> range.getLocationFromCsv().contains(city.getName()))
                .findFirst();
    }
}
