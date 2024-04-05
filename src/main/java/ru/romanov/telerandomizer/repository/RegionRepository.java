package ru.romanov.telerandomizer.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.telerandomizer.entity.Region;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

    @EntityGraph(value = "graph.Region.cities.cityCodes")
    List<Region> findAllByIdNotNull();

}