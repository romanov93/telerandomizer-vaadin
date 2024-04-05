package ru.romanov.telerandomizer.layouts;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.Getter;
import ru.romanov.telerandomizer.views.RegistrationView;

import java.util.stream.Stream;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_SUCCESS;

@SpringComponent
@RouteScope
@RouteScopeOwner(RegistrationView.class)
@Getter

public class RegistrationForm extends FormLayout {

    private H3 title = new H3("Регистрация");
    private TextField nameField = new TextField("Имя");
    private EmailField emailField = new EmailField("Email");
    private PasswordField passwordField = new PasswordField("Пароль");
    private PasswordField passwordConfirmField = new PasswordField("Подтвердите пароль");
    private Span errorMessageField = new Span();
    private Button submitButton = new Button("Зарегистрироваться");
    private Button goToLoginButton = new Button("Войти в аккаунт");

    public RegistrationForm() {
        emailField.setErrorMessage("Неверный email");
        setRequiredIndicatorVisible(nameField, emailField, passwordField, passwordConfirmField);

        submitButton.addThemeVariants(LUMO_PRIMARY);
        goToLoginButton.addThemeVariants(LUMO_PRIMARY, LUMO_SUCCESS);

        add(title, nameField, emailField, passwordField, passwordConfirmField, errorMessageField, submitButton, goToLoginButton);

        setMaxWidth("500px");

        setResponsiveSteps(
                new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
                new ResponsiveStep("490px", 2, ResponsiveStep.LabelsPosition.TOP));

        setColspan(title, 2);
        setColspan(nameField, 2);
        setColspan(emailField, 2);
        setColspan(errorMessageField, 2);
        setColspan(submitButton, 2);
        setColspan(goToLoginButton, 2);
    }

    private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
        Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
    }

}
