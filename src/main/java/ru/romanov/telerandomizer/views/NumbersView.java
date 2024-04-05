package ru.romanov.telerandomizer.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import ru.romanov.telerandomizer.MainAppLayout;
import ru.romanov.telerandomizer.service.SecurityService;
import ru.romanov.telerandomizer.binders.NumbersBinder;
import ru.romanov.telerandomizer.layouts.NumbersLayout;

@Route(value ="numbers", layout = MainAppLayout.class)
@PageTitle("Мои номера")
@RolesAllowed({"ADMIN", "USER"})
@Slf4j
public class NumbersView extends VerticalLayout implements BeforeEnterObserver {

    private final SecurityService securityService;

    public NumbersView(NumbersLayout numbersLayout, NumbersBinder numbersBinder, SecurityService securityService) {
        this.securityService = securityService;
        add(numbersLayout);
        numbersBinder.addBinding();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        securityService.maybeUser().ifPresent(
                user -> log.info("Юзер {} перешел на страницу просмотра номеров", user.getName()));
    }
}