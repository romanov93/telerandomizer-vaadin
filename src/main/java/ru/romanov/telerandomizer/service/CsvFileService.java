package ru.romanov.telerandomizer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.romanov.telerandomizer.entity.CsvFile;
import ru.romanov.telerandomizer.repository.CsvFileRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CsvFileService {

    private final CsvFileRepository csvFileRepository;

    public Optional<CsvFile> getLastVersionByName(String name) {
        List<CsvFile> allWithName = csvFileRepository.findAllByNameContaining(name);
        return allWithName.stream().max(Comparator.comparing(CsvFile::getVersion));
    }

    public void save(CsvFile csvFile) {
        csvFileRepository.save(csvFile);
    }
}