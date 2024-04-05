package ru.romanov.telerandomizer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.romanov.telerandomizer.csvwork.PhoneRangeUpdater;
import ru.romanov.telerandomizer.entity.PhoneRange;
import ru.romanov.telerandomizer.repository.PhoneRangeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhoneRangeService {

    private final PhoneRangeRepository rangeOfPhonesRepository;

    public void saveAll(List<PhoneRange> rangeOfPhonesList) {
        rangeOfPhonesRepository.saveAll(rangeOfPhonesList);
    }

    public List<PhoneRange> findAllByRegionAndNumbersType(String regionName, int numbersType) {
        return rangeOfPhonesRepository.findAllByRegionNameAndNumbersType(regionName, numbersType);
    }

    public List<PhoneRange> findAllByRegionAndCityAndNumbersType(String regionName, String cityName, int numbersType) {
        return rangeOfPhonesRepository.findAllByRegionNameAndCityNameAndNumbersType(regionName, cityName, numbersType);
    }
}
