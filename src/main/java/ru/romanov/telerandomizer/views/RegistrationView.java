package ru.romanov.telerandomizer.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import ru.romanov.telerandomizer.binders.RegistrationBinder;
import ru.romanov.telerandomizer.layouts.RegistrationForm;

@Route("register")
@RouteAlias("registration")
@PageTitle("Регистрация")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {

    public RegistrationView(RegistrationForm registrationForm, RegistrationBinder registrationBinder) {
        add(registrationForm);

        setHorizontalComponentAlignment(Alignment.CENTER, registrationForm);

        registrationBinder.addBindingAndValidation();
    }
}
