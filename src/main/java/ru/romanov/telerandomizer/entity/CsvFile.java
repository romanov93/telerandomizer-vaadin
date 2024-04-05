package ru.romanov.telerandomizer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.File;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "csv_file")
public class CsvFile {

    public static final String PATH_TO_FILES = "csv/";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "version")
    private int version;

    @Column(name = "download_date")
    private LocalDate downloadDate;

    public File loadFile() {
        return new File(PATH_TO_FILES + name);
    }
}