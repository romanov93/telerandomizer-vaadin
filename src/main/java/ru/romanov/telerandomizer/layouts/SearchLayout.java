package ru.romanov.telerandomizer.layouts;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.Getter;
import ru.romanov.telerandomizer.vaadincomponents.TypeChangeRadioButton;
import ru.romanov.telerandomizer.views.SearchView;

import static com.vaadin.flow.component.button.ButtonVariant.*;

@SpringComponent
@RouteScope
@RouteScopeOwner(SearchView.class)
@Getter
public class SearchLayout extends VerticalLayout {

    private TypeChangeRadioButton typeChangeRadioButton = new TypeChangeRadioButton();
    private ComboBox<String> regionComboBox = new ComboBox<>("Регион");
    private ComboBox<String> cityComboBox = new ComboBox<>("Населенный пункт");
    private IntegerField quantityPhonesField = new IntegerField("Сколько телефонов искать");
    private Button infoButton = new Button("Справка");
    private Button searchButton = new Button("Искать!");
    private HorizontalLayout buttonsBar = new HorizontalLayout(searchButton, infoButton);

    public SearchLayout() {
        searchButton.addThemeVariants(LUMO_PRIMARY, LUMO_SUCCESS, LUMO_LARGE);
        infoButton.addThemeVariants(LUMO_SUCCESS, LUMO_LARGE);
        quantityPhonesField.setWidth("215px");
        regionComboBox.setWidth("300px");
        cityComboBox.setWidth("300px");
        regionComboBox.getStyle().set("--vaadin-input-field-border-width", "1px");
        cityComboBox.getStyle().set("--vaadin-input-field-border-width", "1px");
        quantityPhonesField.getStyle().set("--vaadin-input-field-border-width", "1px");
        cityComboBox.setHelperText("(только для домашних номеров)");

        add(typeChangeRadioButton, regionComboBox, cityComboBox, quantityPhonesField, buttonsBar);
    }
}