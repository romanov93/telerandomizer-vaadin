package ru.romanov.telerandomizer.csvwork;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.romanov.telerandomizer.entity.CsvFile;
import ru.romanov.telerandomizer.entity.PhoneRange;
import ru.romanov.telerandomizer.service.CsvFileService;
import ru.romanov.telerandomizer.service.PhoneRangeService;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ru.romanov.telerandomizer.entity.CsvFile.PATH_TO_FILES;
import static ru.romanov.telerandomizer.utils.ListsUtils.findStringsDifference;

@Component
@RequiredArgsConstructor
@Slf4j
public class PhoneRangeUpdater {
    private static final String URL = "https://opendata.digital.gov.ru/downloads/";
    private static final List<String> FILE_NAMES = List.of("ABC-3xx.csv", "ABC-4xx.csv", "ABC-8xx.csv", "DEF-9xx.csv");
    private final HttpsDownloader downloader;
    private final CsvReader csvReader;
    private final CsvFileService csvFileService;
    private final PhoneRangeExtractor phoneRangeExtractor;
    private final PhoneRangeService phoneRangeService;

    @SneakyThrows
    public void update() {
        log.info("Запущен плановый поиск новых диапазонов номеров.");
        for (String fileName : FILE_NAMES) {
            @Cleanup ByteArrayInputStream newCsvByteArrayInputStream =
                    downloader.loadByteArrayInputStreamByURL(URL + fileName);
            Optional<CsvFile> previousCsvFile = csvFileService.getLastVersionByName(fileName);
            previousCsvFile.ifPresentOrElse(
                    previousCsv -> parseCsvRepeatedly(previousCsv, fileName, newCsvByteArrayInputStream),
                    () -> parseCsvFirstTime(fileName, newCsvByteArrayInputStream)
            );
        }
    }

    @SneakyThrows
    private void parseCsvFirstTime(String fileName, ByteArrayInputStream newCsvByteArrayInputStream) {
        log.info("Файл с именем {} не найден в базе, выполняется первичная загрузка", fileName);
        List<String> newRows = csvReader.read(newCsvByteArrayInputStream);
        newCsvByteArrayInputStream.reset();
        int fileVersion = 1;
        String nameOfNewFile = "V" + fileVersion + "__" + fileName;
        Files.copy(newCsvByteArrayInputStream, new File(PATH_TO_FILES + nameOfNewFile).toPath());
        log.info("Сохранен файл {}", nameOfNewFile);
        extractAndSaveNewRanges(nameOfNewFile, newRows, fileVersion);
    }

    @SneakyThrows
    private void parseCsvRepeatedly(CsvFile previousCsv, String fileName, ByteArrayInputStream newCsvByteArrayInputStream) {
        List<String> rowsOfPreviousCsv = csvReader.read(previousCsv.loadFile());
        List<String> rowsOfNewCsv = csvReader.read(newCsvByteArrayInputStream);
        newCsvByteArrayInputStream.reset();
        List<String> newRows = findStringsDifference(rowsOfPreviousCsv, rowsOfNewCsv);
        if (newRows.size() == 0)
            log.info("Файл с именем {} не изменился с прошлой проверки, нет новых диапазонов", fileName);
        else {
            log.info("Файл с именем {} обновился с прошлой проверки, идет поиск новых диапазонов", fileName);
            int fileVersion = previousCsv.getVersion() + 1;
            String nameOfNewFile = "V" + fileVersion + "__" + fileName;
            Files.copy(newCsvByteArrayInputStream, new File(PATH_TO_FILES + nameOfNewFile).toPath());
            log.info("Сохранен файл {}", nameOfNewFile);
            extractAndSaveNewRanges(nameOfNewFile, newRows, fileVersion);
        }
    }

    private void extractAndSaveNewRanges(String fileName, List<String> rows, int fileVersion) {
        csvFileService.save(CsvFile.builder()
                        .name(fileName)
                        .version(fileVersion)
                        .downloadDate(LocalDate.now())
                        .build());
        List<PhoneRange> newRanges = phoneRangeExtractor.parseRows(rows);
        phoneRangeService.saveAll(newRanges);
        log.info("Добавлено {} новых диапазонов из файла {}", newRanges.size(), fileName);
    }
}