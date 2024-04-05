package ru.romanov.telerandomizer.vaadincomponents;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.progressbar.ProgressBar;

public class WaitingDialog extends Dialog {
    private ProgressBar progressBar = new ProgressBar();
    private Div progressBarSubLabel = new Div();

    public WaitingDialog() {
        setHeaderTitle("Подождите, идет поиск телефонов.");

        progressBar.setIndeterminate(true);

        progressBarSubLabel.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        progressBarSubLabel.setText("Генерация займет несколько секунд");

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);

        add(progressBar, progressBarSubLabel);
    }
}
