package ru.romanov.telerandomizer.binders;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.romanov.telerandomizer.dto.PhoneDTO;
import ru.romanov.telerandomizer.filewriters.CsvWriter;
import ru.romanov.telerandomizer.filewriters.XlsxWriter;
import ru.romanov.telerandomizer.entity.Phone;
import ru.romanov.telerandomizer.mappers.PhoneListMapper;
import ru.romanov.telerandomizer.service.SecurityService;
import ru.romanov.telerandomizer.service.PhoneService;
import ru.romanov.telerandomizer.layouts.NumbersLayout;
import ru.romanov.telerandomizer.views.NumbersView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_SUCCESS;
import static com.vaadin.flow.component.icon.VaadinIcon.DOWNLOAD_ALT;
import static ru.romanov.telerandomizer.entity.Phone.NUMBER_TYPE_HOME;
import static ru.romanov.telerandomizer.entity.Phone.NUMBER_TYPE_MOBILE;
import static ru.romanov.telerandomizer.utils.FileNameHelper.formatFileName;

@SpringComponent
@RouteScope
@RouteScopeOwner(NumbersView.class)
@RequiredArgsConstructor
@Getter
@Slf4j
public class NumbersBinder {

    private final NumbersLayout numbersLayout;
    private final SecurityService securityService;
    private final PhoneService phoneService;
    private final CsvWriter csvWriter;
    private final XlsxWriter xlsxWriter;
    private final PhoneListMapper phoneListMapper;
    private List<PhoneDTO> allCurrentUserPhones;

    public void addBinding() {
        configurePageOpenEvent();
        configureEventsListeners();
    }

    private void configurePageOpenEvent() {
        securityService.maybeUser().ifPresent(user ->
                allCurrentUserPhones = phoneListMapper.toDTOList(phoneService.findAllByUserId(user)));
        numbersLayout.getTypeComboBox().setItems(List.of("Домашние", "Мобильные"));
        numbersLayout.getTypeComboBox().setValue("Домашние");
        numbersLayout.getRegionComboBox().setItems(allCurrentUserPhones.stream()
                .filter(phone -> phone.getNumberType() == getSelectedPhonesType())
                .map(PhoneDTO::getRegionName)
                .collect(Collectors.toSet()));
        filterPhonesInTable();
        configureDownloadingCSV();
        configureDownloadingXLSX();
        numbersLayout.getPhonesTotalCounter().setText(String.valueOf(allCurrentUserPhones.size()));
        numbersLayout.getPhonesInTableCounter().setText(String.valueOf(allCurrentUserPhones.size()));
    }

    private void configureEventsListeners() {
        numbersLayout.getTypeComboBox().addValueChangeListener(valueChangeEvent -> changeTypeAction());

        numbersLayout.getRegionComboBox().addValueChangeListener(valueChangeEvent -> changeRegionAction());

        numbersLayout.getCityComboBox().addValueChangeListener(valueChangeEvent -> changeCityAction());

        numbersLayout.getDateComboBox().addValueChangeListener(valueChangeEvent -> filterPhonesInTable());

        numbersLayout.getPhoneTable().getSaveButton().addClickListener(valueChangeEvent -> saveEditedPhone());
    }

    private void filterPhonesInTable() {
        String region = numbersLayout.getRegionComboBox().getValue();
        String city = numbersLayout.getCityComboBox().getValue();
        String date = numbersLayout.getDateComboBox().getValue();
        int type = getSelectedPhonesType();

        List<PhoneDTO> numbers = new ArrayList<>();

        if      (type == NUMBER_TYPE_HOME && region != null && city == null && date == null)
            numbers = allCurrentUserPhones.stream()
                    .filter(p -> p.getNumberType() == type).filter(p -> p.getRegionName().equals(region)).toList();
        else if (type == NUMBER_TYPE_HOME && region == null && city != null && date == null)
            numbers = allCurrentUserPhones.stream()
                    .filter(p -> p.getNumberType() == type).filter(p -> p.getCityName().equals(city)).toList();
        else if (type == NUMBER_TYPE_HOME && region == null && city == null && date != null)
            numbers = allCurrentUserPhones.stream()
                    .filter(p -> p.getNumberType() == type).filter(p -> p.getSearchDate().equals(date)).toList();
        else if (type == NUMBER_TYPE_HOME && region != null && city != null && date == null)
            numbers = allCurrentUserPhones.stream().filter(p -> p.getNumberType() == type)
                    .filter(p -> p.getRegionName().equals(region)).filter(p -> p.getCityName().equals(city)).toList();
        else if (type == NUMBER_TYPE_HOME && region == null && city != null && date != null)
            numbers = allCurrentUserPhones.stream().filter(p -> p.getNumberType() == type)
                    .filter(p -> p.getCityName().equals(city)).filter(p -> p.getSearchDate().equals(date)).toList();
        else if (type == NUMBER_TYPE_HOME && region != null && city == null && date != null)
            numbers = allCurrentUserPhones.stream().filter(p -> p.getNumberType() == type)
                    .filter(p -> p.getRegionName().equals(region)).filter(p -> p.getSearchDate().equals(date)).toList();
        else if (type == NUMBER_TYPE_HOME && region != null && city != null && date != null)
            numbers = allCurrentUserPhones.stream()
                    .filter(p -> p.getNumberType() == type).filter(p -> p.getRegionName().equals(region))
                    .filter(p -> p.getCityName().equals(city)).filter(p -> p.getSearchDate().equals(date)).toList();
        else if (type == NUMBER_TYPE_HOME && region == null && city == null && date == null)
            numbers = allCurrentUserPhones.stream().filter(p -> p.getNumberType() == type).toList();
        else if (type == NUMBER_TYPE_MOBILE && region == null && city == null && date == null)
            numbers = allCurrentUserPhones.stream().filter(p -> p.getNumberType() == type).toList();
        else if (type == NUMBER_TYPE_MOBILE && region != null && city == null && date != null)
            numbers = allCurrentUserPhones.stream().filter(p -> p.getNumberType() == type)
                    .filter(p -> p.getRegionName().equals(region)).filter(p -> p.getSearchDate().equals(date)).toList();
        else if (type == NUMBER_TYPE_MOBILE && region != null && city == null && date == null)
            numbers = allCurrentUserPhones.stream()
                    .filter(p -> p.getNumberType() == type).filter(p -> p.getRegionName().equals(region)).toList();
        else if (type == NUMBER_TYPE_MOBILE && region == null && city == null && date != null)
            numbers = allCurrentUserPhones.stream()
                    .filter(p -> p.getNumberType() == type).filter(p -> p.getSearchDate().equals(date)).toList();

        numbersLayout.getPhoneTable().setItems(numbers);
        numbersLayout.getPhonesInTableCounter().setText(String.valueOf(numbers.size()));
        String fileName = formatFileName(region, city);
        numbersLayout.getDownloadingXlsx().setHref(xlsxWriter.streamResource(numbers, fileName));
        numbersLayout.getDownloadingCsv().setHref(csvWriter.streamResource(numbers, fileName));
    }

    private void configureDownloadingXLSX() {
        Button downloadButton = new Button("Скачать таблицу xlsx", new Icon(DOWNLOAD_ALT));
        downloadButton.addThemeVariants(LUMO_PRIMARY, LUMO_SUCCESS);
        numbersLayout.getDownloadingXlsx().add(downloadButton);

        StreamResource streamResource = xlsxWriter.streamResource(numbersLayout.getPhoneTable(), "Телефоны");
        numbersLayout.getDownloadingXlsx().setHref(streamResource);
        downloadButton.addClickListener(click -> logDownloading("XLSX"));
    }

    private void configureDownloadingCSV() {
        Button downloadButton = new Button("Скачать таблицу csv", new Icon(DOWNLOAD_ALT));
        downloadButton.addThemeVariants(LUMO_PRIMARY, LUMO_SUCCESS);
        numbersLayout.getDownloadingCsv().add(downloadButton);

        StreamResource streamResource = csvWriter.streamResource(numbersLayout.getPhoneTable(), "Телефоны");
        numbersLayout.getDownloadingCsv().setHref(streamResource);
        downloadButton.addClickListener(click -> logDownloading("CSV"));
    }

    private void logDownloading(String format) {
        log.info("Юзер {} скачал файл в формате: {} с типом номеров: {} по региону: {}, по городу: {}, за дату: {}",
                securityService.maybeUser().get().getName(),
                format,
                getSelectedPhonesType(),
                numbersLayout.getRegionComboBox().getValue(),
                numbersLayout.getCityComboBox().getValue(),
                numbersLayout.getDateComboBox().getValue());
    }

    private void changeTypeAction() {
        if (numbersLayout.getTypeComboBox().isEmpty()) {
            numbersLayout.getTypeComboBox().setValue("Домашние");
            changeTypeAction();
        } else if (getSelectedPhonesType() == NUMBER_TYPE_MOBILE) {
            numbersLayout.getCityComboBox().setReadOnly(true);
            numbersLayout.getPhoneTable().getCityColumn().setVisible(false);
            numbersLayout.getPhoneTable().getRegionColumn().setWidth("500px");
        } else if (getSelectedPhonesType() == NUMBER_TYPE_HOME) {
            numbersLayout.getCityComboBox().setReadOnly(false);
            numbersLayout.getPhoneTable().getCityColumn().setVisible(true);
            numbersLayout.getPhoneTable().getRegionColumn().setWidth("200px");
        }
        numbersLayout.getDateComboBox().setItems(Collections.emptyList());
        numbersLayout.getCityComboBox().setItems(Collections.emptyList());
        numbersLayout.getRegionComboBox().setItems(allCurrentUserPhones.stream()
                .filter(phone -> phone.getNumberType() == getSelectedPhonesType())
                .map(PhoneDTO::getRegionName)
                .collect(Collectors.toSet()));
        resetComboBoxChoose(true, true);

        filterPhonesInTable();
    }

    private void changeRegionAction() {
        resetComboBoxChoose(false, true);
        numbersLayout.getDateComboBox().setItems(allCurrentUserPhones.stream()
                .filter(phone -> phone.getNumberType() == getSelectedPhonesType())
                .filter(phone -> phone.getRegionName().equals(numbersLayout.getRegionComboBox().getValue()))
                .map(PhoneDTO::getSearchDate)
                .collect(Collectors.toSet()));
        if (getSelectedPhonesType() == NUMBER_TYPE_HOME)
            numbersLayout.getCityComboBox().setItems(allCurrentUserPhones.stream()
                .filter(phone -> phone.getNumberType() == getSelectedPhonesType())
                .filter(phone -> phone.getRegionName().equals(numbersLayout.getRegionComboBox().getValue()))
                .map(PhoneDTO::getCityName)
                .collect(Collectors.toSet()));
        else
            numbersLayout.getCityComboBox().setItems(Collections.emptyList());

        filterPhonesInTable();
    }

    private void changeCityAction() {
        resetComboBoxChoose(false, false);
        numbersLayout.getDateComboBox().setValue(null);
        numbersLayout.getDateComboBox().setItems(allCurrentUserPhones.stream()
                .filter(phone -> phone.getNumberType() == getSelectedPhonesType())
                .filter(phone -> phone.getRegionName().equals(numbersLayout.getRegionComboBox().getValue()))
                .filter(phone -> phone.getCityName().equals(numbersLayout.getCityComboBox().getValue()))
                .map(PhoneDTO::getSearchDate)
                .collect(Collectors.toSet()));

        filterPhonesInTable();
    }

    private void resetComboBoxChoose(boolean resetRegion, boolean resetCity) {
        numbersLayout.getDateComboBox().setValue(null);
        if (resetRegion) numbersLayout.getRegionComboBox().setValue(null);
        if (resetCity) numbersLayout.getCityComboBox().setValue(null);
    }

    private void saveEditedPhone() {
        String comment = numbersLayout.getPhoneTable().getCommentTextArea().getValue();
        String status = numbersLayout.getPhoneTable().getStatusComboBox().getValue();
        final int MAX_COMMENT_LENGTH = 100;
        if (comment.length() <= MAX_COMMENT_LENGTH) {
            PhoneDTO editedPhoneDTO = numbersLayout.getPhoneTable().getTableEditor().getItem();
            editedPhoneDTO.setStatus(status);
            editedPhoneDTO.setComment(comment);
            numbersLayout.getPhoneTable().getTableEditor().save();

            Optional<Phone> editedPhone = phoneService.findByUserAndNumber(
                    securityService.maybeUser().get(),
                    editedPhoneDTO.getPhoneNumber());

            editedPhone.ifPresent(phone -> {
                phone.setStatus(status);
                phone.setComment(comment);
                phoneService.update(phone);
            });
        }
    }

    private int getSelectedPhonesType() {
        String type = numbersLayout.getTypeComboBox().getValue();
        if (type.equals("Домашние"))
            return NUMBER_TYPE_HOME;
        else
            return NUMBER_TYPE_MOBILE;
    }
}