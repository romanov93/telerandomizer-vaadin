package ru.romanov.telerandomizer.layouts;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.Getter;
import ru.romanov.telerandomizer.vaadincomponents.PhoneTable;
import ru.romanov.telerandomizer.views.NumbersView;

@SpringComponent
@RouteScope
@RouteScopeOwner(NumbersView.class)
@Getter

public class NumbersLayout extends VerticalLayout {

    private PhoneTable phoneTable = new PhoneTable();
    private ComboBox<String> typeComboBox = new ComboBox<>();
    private ComboBox<String> regionComboBox = new ComboBox<>();
    private ComboBox<String> cityComboBox = new ComboBox<>();
    private ComboBox<String> dateComboBox = new ComboBox<>();
    private Span prefixSpan = new Span("Всего номеров найдено: ");
    private Span phonesTotalCounter = new Span();
    private Span prefixSpan2 = new Span("Номеров в таблице: ");
    private Span phonesInTableCounter = new Span();
    private HorizontalLayout counterLayout
            = new HorizontalLayout(prefixSpan, phonesTotalCounter);
    private HorizontalLayout counterLayout2
            = new HorizontalLayout(prefixSpan2, phonesInTableCounter);
    private HorizontalLayout topPanel = new HorizontalLayout(typeComboBox, regionComboBox, cityComboBox,
            dateComboBox, counterLayout, counterLayout2);
    private Anchor downloadingXlsx = new Anchor();
    private Anchor downloadingCsv = new Anchor();
    private HorizontalLayout footerPanel = new HorizontalLayout(downloadingXlsx, downloadingCsv);

    public NumbersLayout() {
        counterLayout.setSpacing(false);
        counterLayout2.setSpacing(false);

        regionComboBox.setClearButtonVisible(true);
        cityComboBox.setClearButtonVisible(true);
        dateComboBox.setClearButtonVisible(true);

        prefixSpan.getElement().getThemeList().add("badge contrast");
        phonesTotalCounter.getElement().getThemeList().add("badge success");
        prefixSpan2.getElement().getThemeList().add("badge contrast");
        phonesInTableCounter.getElement().getThemeList().add("badge success");

        typeComboBox.setPlaceholder("Тип номеров");
        regionComboBox.setPlaceholder("Регион");
        cityComboBox.setPlaceholder("Населенный пункт");
        dateComboBox.setPlaceholder("Дата поиска");

        typeComboBox.getStyle().set("--vaadin-input-field-border-width", "1px");
        regionComboBox.getStyle().set("--vaadin-input-field-border-width", "1px");
        cityComboBox.getStyle().set("--vaadin-input-field-border-width", "1px");
        dateComboBox.getStyle().set("--vaadin-input-field-border-width", "1px");

        add(topPanel, phoneTable, footerPanel);
    }
}