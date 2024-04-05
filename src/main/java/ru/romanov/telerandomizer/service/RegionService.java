package ru.romanov.telerandomizer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.romanov.telerandomizer.entity.Region;
import ru.romanov.telerandomizer.repository.RegionRepository;

import java.util.List;

@Service
@Order(1)
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    public List<Region> findAllWithCitiesAndCityCodes() {
        return regionRepository.findAllByIdNotNull();
    }

    public List<Region> findAll() {
        return regionRepository.findAll();
    }

}