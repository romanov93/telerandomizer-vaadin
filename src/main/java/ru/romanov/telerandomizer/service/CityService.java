package ru.romanov.telerandomizer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.romanov.telerandomizer.entity.City;
import ru.romanov.telerandomizer.repository.CityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public List<City> findAllByRegionName(String regionName) {
        return cityRepository.findAllByRegionName(regionName);
    }
}
