package ru.romanov.telerandomizer.filewriters;

import com.vaadin.flow.server.StreamResource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.romanov.telerandomizer.dto.PhoneDTO;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;

@Component
public class CsvWriter implements FileWriter<PhoneDTO> {

    private final static String FIRST_ROW = "\"Номер\";\"Регион\";\"Город\";\"Статус\";\"Комментарий\";\"Дата поиска\"\n";
    private final static String EMPTY_STRING = "";
    private final static String CHARSET = "windows-1251";
    private final static String EXTENSION = ".csv";
    private final static String DELIMITER = ";";

    @Override
    public StreamResource streamResource(List<PhoneDTO> phones, String fileName) {
        return new StreamResource(fileName + EXTENSION, () -> csvInBytesArrayInputStream(phones));
    }

    @SneakyThrows
    private ByteArrayInputStream csvInBytesArrayInputStream(List<PhoneDTO> phones) {
        return new ByteArrayInputStream(phonesToString(phones).getBytes(CHARSET));
    }

    private String phonesToString(Collection<PhoneDTO> phonesFromTable) {
        StringBuilder stringBuilder = new StringBuilder().append(FIRST_ROW);

        for (PhoneDTO phoneDTO : phonesFromTable) {
            stringBuilder
                    .append("\"").append(phoneDTO.getPhoneNumber()).append("\"").append(DELIMITER)
                    .append("\"").append(phoneDTO.getRegionName()).append("\"").append(DELIMITER)
                    .append("\"").append(phoneDTO.getCityName()).append("\"").append(DELIMITER)
                    .append("\"").append(phoneDTO.getStatus()).append("\"").append(DELIMITER)
                    .append("\"").append(phoneDTO.getComment()).append("\"").append(DELIMITER)
                    .append("\"").append(phoneDTO.getSearchDate()).append("\"").append(DELIMITER)
                    .append("\n");
        }

        return stringBuilder.toString().replace("null", EMPTY_STRING);
    }
}