package ru.romanov.telerandomizer.definers;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.romanov.telerandomizer.entity.City;
import ru.romanov.telerandomizer.entity.Region;
import ru.romanov.telerandomizer.service.RegionService;

import java.util.*;

/** Данный класс на старте приложения загружает из БД информацию о регионах, городах и их телефонных кодах по РФ,
 * что вполедствии в десятки раз ускоряет к ней доступ.
 * Эти данные нужны при парсинге csv-файлов с сайта Минцифры, содержащих диапазоны телефонных номеров, для определения
 * к какому региону/городу относится каждый отдельный диапазон. */

@Component
@Order(2)
@RequiredArgsConstructor
@Getter
public class RegionDataStorage {

    private final RegionService regionService;
    private List<Region> allRegions;
    private Map<String, Region> regionByPartOfName;
    private Map<Integer, Region> regionByCode = new HashMap<>();
    private Map<Integer, Set<City>> citiesByRegionId = new HashMap<>();
    private Map<Integer, Map<Integer, City>> cityByCodeByRegionId = new HashMap<>();

    @PostConstruct
    public void initData() {
        allRegions = regionService.findAllWithCitiesAndCityCodes();
        allRegions.sort(Comparator.comparing(Region::getId));

        allRegions.forEach(region -> regionByCode.put(region.getCode(), region));

        allRegions.forEach(region -> citiesByRegionId.put(region.getId(), region.getCities()));

        for (Region region : allRegions) {
            Set<City> cities = region.getCities();
            Map<Integer, City> subMap = new HashMap<>();
            cities.forEach(city -> city.getCodes().forEach(code -> subMap.put(code.getCode(), city)));
            cityByCodeByRegionId.put(region.getId(), subMap);
        }

        regionByPartOfName = Map.of(
                "Чеченская", allRegions.get(87),
                "Ханты", allRegions.get(85),
                "Карачаево", allRegions.get(19),
                "Удмуртская", allRegions.get(82),
                "Республика Саха", allRegions.get(61),
                "Крым", allRegions.get(57),
                "Севастополь", allRegions.get(58),
                "Чувашская", allRegions.get(88),
                "Еврейская", allRegions.get(11));
    }
}