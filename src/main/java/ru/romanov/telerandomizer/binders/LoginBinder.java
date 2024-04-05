package ru.romanov.telerandomizer.binders;

import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.romanov.telerandomizer.layouts.LoginLayout;
import ru.romanov.telerandomizer.views.LoginView;

import static ru.romanov.telerandomizer.utils.PageRouteUtils.REGISTRATION_PAGE_ROUTE;

@SpringComponent
@RouteScope
@RouteScopeOwner(LoginView.class)
@RequiredArgsConstructor
@Slf4j
public class LoginBinder {
    private final LoginLayout loginLayout;

    public void addBinding() {
        loginLayout.getRegistrationButton().addClickListener(click ->
                loginLayout.getUI().ifPresent(ui -> ui.navigate(REGISTRATION_PAGE_ROUTE)));
    }
}