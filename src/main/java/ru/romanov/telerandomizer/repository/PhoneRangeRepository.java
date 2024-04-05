package ru.romanov.telerandomizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.romanov.telerandomizer.entity.PhoneRange;

import java.util.List;

public interface PhoneRangeRepository extends JpaRepository<PhoneRange, Integer> {

    List<PhoneRange> findAllByRegionNameAndNumbersType(String region, int numbersType);

    List<PhoneRange> findAllByRegionNameAndCityNameAndNumbersType(String region, String city, int numbersType);
}
