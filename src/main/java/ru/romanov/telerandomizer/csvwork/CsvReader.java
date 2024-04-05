package ru.romanov.telerandomizer.csvwork;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class CsvReader {

    @SneakyThrows
    public List<String> read(InputStream inputStream) {
        List<String> rows = new ArrayList<>();
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
        reader.readLine(); // Пропускаем строку с именем колонок.
        String row;
        while ((row = reader.readLine()) != null)
            rows.add(row);
        return rows;
    }

    @SneakyThrows
    public List<String> read(File csvFile) {
        return read(new FileInputStream(csvFile));
    }
}