package ru.romanov.telerandomizer.filewriters;

import com.vaadin.flow.server.StreamResource;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;
import ru.romanov.telerandomizer.dto.PhoneDTO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class XlsxWriter implements FileWriter<PhoneDTO> {

    private final static String EXTENSION = ".xlsx";
    @Override
    public StreamResource streamResource(List<PhoneDTO> phones, String fileName) {
        return new StreamResource(fileName + EXTENSION, () -> xlsxByteArrayInputStream(phones));
    }

    private ByteArrayInputStream xlsxByteArrayInputStream(List<PhoneDTO> phones) {
        return new ByteArrayInputStream(getXlsxBytesArray(getXlsxWorkbook(phones)));
    }

    @SneakyThrows
    private byte[] getXlsxBytesArray(Workbook workbook) {
        @Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);

        return bos.toByteArray();
    }

    private Workbook getXlsxWorkbook(List<PhoneDTO> phones) {
        Workbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet("Телефоны");
        sheet.setColumnWidth(0, 3500);
        sheet.setColumnWidth(1, 8000);
        sheet.setColumnWidth(2, 8000);
        sheet.setColumnWidth(3,3500);
        sheet.setColumnWidth(4,8000);
        sheet.setColumnWidth(5,5000);
        Row titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue("Телефон");
        titleRow.createCell(1).setCellValue("Регион");
        titleRow.createCell(2).setCellValue("Населенный пункт");
        titleRow.createCell(3).setCellValue("Статус звонка");
        titleRow.createCell(4).setCellValue("Комментарий");
        titleRow.createCell(5).setCellValue("Дата поиска");
        int rowCounter = 1;
        for(PhoneDTO phoneDTO : phones) {
            Row row = sheet.createRow(rowCounter);
            row.createCell(0).setCellValue(phoneDTO.getPhoneNumber());
            row.createCell(1).setCellValue(phoneDTO.getRegionName());
            row.createCell(2).setCellValue(phoneDTO.getCityName());
            row.createCell(3).setCellValue(phoneDTO.getStatus());
            row.createCell(4).setCellValue(phoneDTO.getComment());
            row.createCell(5).setCellValue(phoneDTO.getSearchDate());
            rowCounter++;
        }
        return workbook;
    }
}
