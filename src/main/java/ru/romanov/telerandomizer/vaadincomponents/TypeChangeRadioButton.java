package ru.romanov.telerandomizer.vaadincomponents;

import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

import static com.vaadin.flow.component.radiobutton.RadioGroupVariant.LUMO_VERTICAL;
import static ru.romanov.telerandomizer.entity.Phone.NUMBER_TYPE_HOME;
import static ru.romanov.telerandomizer.entity.Phone.NUMBER_TYPE_MOBILE;

public class TypeChangeRadioButton extends RadioButtonGroup<String> {

    public TypeChangeRadioButton() {
        addThemeVariants(LUMO_VERTICAL);
        setLabel("Домашние или мобильные номера?");
        setItems("Домашние", "Мобильные");
        setValue("Домашние");
    }

    public int getSelectedType() {
        if (getValue().equals("Домашние"))
            return NUMBER_TYPE_HOME;
        else
            return NUMBER_TYPE_MOBILE;
    }
}