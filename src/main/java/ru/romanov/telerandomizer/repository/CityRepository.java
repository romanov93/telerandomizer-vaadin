package ru.romanov.telerandomizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.telerandomizer.entity.City;

import java.util.List;


@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    List<City> findAllByRegionName(String regionName);

}