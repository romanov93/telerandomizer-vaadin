package ru.romanov.telerandomizer.vaadincomponents;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;

public class FailureDialog extends Dialog {

    private Span message = new Span("Пожалуйста, повторите запрос или обратитесь в поддержу.");
    private Button closeButton = new Button("Ок!", click -> close());

    public FailureDialog() {
        setHeaderTitle("Извините, что-то пошло не так.");

        add(message);

        getFooter().add(closeButton);
    }
}