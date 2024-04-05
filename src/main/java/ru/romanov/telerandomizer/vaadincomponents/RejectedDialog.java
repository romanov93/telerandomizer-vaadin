package ru.romanov.telerandomizer.vaadincomponents;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;

public class RejectedDialog extends Dialog {

    private Button closeButton = new Button("Ok", click -> close());

    private Span infoSpan = new Span();

    public RejectedDialog(int quantity) {
        setHeaderTitle("Недостаточно номеров");
        infoSpan.setText("По запрошенным параметрам есть только " + quantity + numbers(quantity));
        add(infoSpan);
        getFooter().add(closeButton);
    }

    private String numbers(int quantity) {
        if (
                (quantity % 10 == 2 && quantity % 100 != 12) ||
                (quantity % 10 == 3 && quantity % 100 != 13) ||
                (quantity % 10 == 4 && quantity % 100 != 14)
        )
            return " номера";
        else if (quantity % 10 == 1 && quantity % 100 != 11)
            return " номер";
        else
            return " номеров";
    }
}