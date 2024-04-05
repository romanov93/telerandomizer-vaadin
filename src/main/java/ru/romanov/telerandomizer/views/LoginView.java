package ru.romanov.telerandomizer.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.slf4j.Slf4j;
import ru.romanov.telerandomizer.binders.LoginBinder;
import ru.romanov.telerandomizer.layouts.LoginLayout;

@Route("login")
@PageTitle("Вход")
@AnonymousAllowed
@Slf4j
public class LoginView extends VerticalLayout {

    public LoginView(LoginLayout loginLayout, LoginBinder loginBinder) {
        add(loginLayout);

        loginBinder.addBinding();
    }
}