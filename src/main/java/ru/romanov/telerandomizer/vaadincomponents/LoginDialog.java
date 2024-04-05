package ru.romanov.telerandomizer.vaadincomponents;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.vaadin.flow.component.button.ButtonVariant.*;

@Slf4j
@Getter
public class LoginDialog extends Dialog {

    private Button registrationButton = new Button("Регистрация");
    private Button closeButton = new Button("Закрыть", click -> close());
    private MyLoginForm loginForm = new MyLoginForm();

    public LoginDialog() {
        registrationButton.addClickListener(click -> {
            getUI().ifPresent(ui -> ui.navigate("registration"));
            close();
        });
        registrationButton.addThemeVariants(LUMO_PRIMARY, LUMO_SUCCESS, LUMO_LARGE);

        closeButton.addThemeVariants(LUMO_PRIMARY, LUMO_ERROR, LUMO_LARGE);

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);

        add(loginForm);
        getFooter().add(registrationButton, closeButton);
    }
}
