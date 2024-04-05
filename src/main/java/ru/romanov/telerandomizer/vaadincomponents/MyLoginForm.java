package ru.romanov.telerandomizer.vaadincomponents;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyLoginForm extends LoginForm {

    private LoginI18n i18n = LoginI18n.createDefault();
    private LoginI18n.Form loginI18n = i18n.getForm();

    public MyLoginForm() {
        loginI18n.setTitle("Вход");
        loginI18n.setUsername("Имя");
        loginI18n.setPassword("Пароль");
        loginI18n.setSubmit("Войти");

        setForgotPasswordButtonVisible(false);

        setAction("login");
        i18n.setForm(loginI18n);
        setI18n(i18n);

        addLoginListener(loginEvent -> log.info("Юзер {} нажал кнопку входа", loginEvent.getUsername()));
    }
}
