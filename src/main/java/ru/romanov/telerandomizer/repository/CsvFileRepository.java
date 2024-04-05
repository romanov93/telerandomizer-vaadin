package ru.romanov.telerandomizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.telerandomizer.entity.CsvFile;

import java.util.List;

@Repository
public interface CsvFileRepository extends JpaRepository<CsvFile, Integer> {

    List<CsvFile> findAllByNameContaining(String fileName);
}