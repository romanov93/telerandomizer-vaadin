package ru.romanov.telerandomizer.vaadincomponents;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.extern.slf4j.Slf4j;
import ru.romanov.telerandomizer.dto.PhoneDTO;
import ru.romanov.telerandomizer.filewriters.CsvWriter;
import ru.romanov.telerandomizer.filewriters.XlsxWriter;
import ru.romanov.telerandomizer.randomizer.UserRequest;

import java.util.List;

import static com.vaadin.flow.component.button.ButtonVariant.*;
import static ru.romanov.telerandomizer.entity.Phone.NUMBER_TYPE_MOBILE;
import static ru.romanov.telerandomizer.utils.FileNameHelper.formatFileName;

@Slf4j
public class ResultDialog extends Dialog {

    private final List<PhoneDTO> tableData;
    private final XlsxWriter xlsxWriter;
    private final CsvWriter csvWriter;
    private final UserRequest userRequest;
    private ResultTable table = new ResultTable();
    private Anchor downloadXlsxAnchor = new Anchor();
    private Anchor downloadCsvAnchor = new Anchor();
    private Button closeButton = new Button("Закрыть", click -> close());
    private HorizontalLayout buttonsLayout = new HorizontalLayout(downloadXlsxAnchor, downloadCsvAnchor, closeButton);
    private String fileName;

    public ResultDialog(List<PhoneDTO> tableData,
                        XlsxWriter xlsxWriter,
                        CsvWriter csvWriter,
                        UserRequest userRequest) {
        this.tableData = tableData;
        this.xlsxWriter = xlsxWriter;
        this.csvWriter = csvWriter;
        this.userRequest = userRequest;
        fileName = formatFileName(userRequest.getRequiredRegionName(), userRequest.getRequiredCityName());

        if (tableData.get(0).getNumberType() == NUMBER_TYPE_MOBILE)
            table.getColumns().get(2).setVisible(false);
        setWidth("650px");
        setCloseOnOutsideClick(false);
        table.setItems(tableData);
        configureDownloadingXLSX();
        configureDownloadingCSV();
        closeButton.setWidth("190px");
        closeButton.addThemeVariants(LUMO_PRIMARY, LUMO_ERROR);

        add(table, buttonsLayout);
    }

    private void configureDownloadingXLSX() {
        Button downloadButton = new Button("Скачать таблицу xlsx", new Icon(VaadinIcon.DOWNLOAD_ALT));
        downloadButton.setWidth("200px");
        downloadButton.addThemeVariants(LUMO_PRIMARY, LUMO_SUCCESS);
        downloadXlsxAnchor.add(downloadButton);
        downloadXlsxAnchor.setHref(xlsxWriter.streamResource(tableData, fileName));
        downloadButton.addClickListener(click -> logDownloading("XLSX"));
    }

    private void configureDownloadingCSV() {
        Button downloadButton = new Button("Скачать таблицу csv", new Icon(VaadinIcon.DOWNLOAD_ALT));
        downloadButton.setWidth("200px");
        downloadButton.addThemeVariants(LUMO_PRIMARY, LUMO_SUCCESS);
        downloadCsvAnchor.add(downloadButton);
        downloadCsvAnchor.setHref(csvWriter.streamResource(tableData, fileName));
        downloadButton.addClickListener(click -> logDownloading("CSV"));
    }

    private void logDownloading(String fileFormat) {
        userRequest.getMaybeUser().ifPresentOrElse(
                user -> log.info(
                        "Юзер {} скачал {} файл с типом номеров: {} по региону: {}, по городу: {}, в количестве: {}",
                        user.getName(),
                        fileFormat,
                        userRequest.getRequiredPhoneType(),
                        userRequest.getRequiredRegionName(),
                        userRequest.getRequiredCityName(),
                        userRequest.getRequiredQuantity()),
                () -> log.info(
                        "Гость скачал {} файл с типом номеров: {} по региону: {}, по городу: {}, в количестве: {}",
                        fileFormat,
                        userRequest.getRequiredPhoneType(),
                        userRequest.getRequiredRegionName(),
                        userRequest.getRequiredCityName(),
                        userRequest.getRequiredQuantity())
        );
    }
}
