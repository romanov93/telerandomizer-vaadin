package ru.romanov.telerandomizer.binders;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.romanov.telerandomizer.entity.User;
import ru.romanov.telerandomizer.service.UserService;
import ru.romanov.telerandomizer.views.LoginView;
import ru.romanov.telerandomizer.layouts.RegistrationForm;
import ru.romanov.telerandomizer.views.RegistrationView;

import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_SUCCESS;
import static ru.romanov.telerandomizer.utils.PageRouteUtils.LOGIN_PAGE_ROUTE;

@SpringComponent
@RouteScope
@RouteScopeOwner(RegistrationView.class)
@Slf4j
@RequiredArgsConstructor

public class RegistrationBinder {

    private final RegistrationForm registrationForm;
    private final PasswordEncoder encoder;
    private final UserService userService;
    private BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

    public void addBindingAndValidation() {
        binder.forField(registrationForm.getPasswordField()).withValidator(this::passwordValidator).bind("password");
        binder.forField(registrationForm.getNameField()).withValidator(this::usernameValidator).bind("name");
        binder.bindInstanceFields(registrationForm);

        registrationForm.getPasswordConfirmField().addValueChangeListener(event -> binder.validate());
        registrationForm.getNameField().addValueChangeListener(event -> binder.validate());

        registrationForm.getGoToLoginButton().addClickListener(event ->
            registrationForm.getUI().ifPresent(ui -> ui.navigate(LOGIN_PAGE_ROUTE)));

        binder.setStatusLabel(registrationForm.getErrorMessageField());

        registrationForm.getSubmitButton().addClickListener(event -> tryRegister());
    }

    private ValidationResult passwordValidator(String pass, ValueContext ctx) {
        if (pass == null || pass.length() < 8)
            return ValidationResult.error("Пароль должен быть не короче 8 символов");

        if (pass.equals(registrationForm.getPasswordConfirmField().getValue()))
            return ValidationResult.ok();
        else
            return ValidationResult.error("Пароли не совпадают");
    }

    private ValidationResult usernameValidator(String name, ValueContext ctx) {
        boolean nameAlreadyUsed = userService.findAll().stream().anyMatch(user -> user.getName().equals(name));

        if (nameAlreadyUsed)
            return ValidationResult.error("Пользователь с таким именем уже есть");
        else if (name.length() < 2 || name.length() > 40)
            return ValidationResult.error("Имя должно быть не короче 2 символов и не длинее 40");
        else
            return ValidationResult.ok();
    }

    private void showSuccess(User userBean) {
        Notification notification =
                Notification.show("Регистрация успешна. Добро пожаловать, " + userBean.getName() + "!");
        notification.addThemeVariants(LUMO_SUCCESS);

        log.info("Зарегистрировался юзер с именем: {}", userBean.getName());
    }

    @SneakyThrows
    private void tryRegister() {
        try {
            User registringUser = new User();
            registringUser.setRole("USER");
            registringUser.setEmail(registrationForm.getEmailField().getValue());
            binder.writeBean(registringUser);
            registringUser.setPassword(encoder.encode(registringUser.getPassword()));
            userService.update(registringUser);
            showSuccess(registringUser);
            registrationForm.getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        } catch (ValidationException e) {
            log.info("Неудачная попытка регистрации");
            throw e;
        }
    }
}