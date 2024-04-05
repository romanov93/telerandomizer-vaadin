package ru.romanov.telerandomizer.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.slf4j.Slf4j;
import ru.romanov.telerandomizer.MainAppLayout;
import ru.romanov.telerandomizer.service.SecurityService;
import ru.romanov.telerandomizer.layouts.InfoLayout;

@Route(value = "information", layout = MainAppLayout.class)
@RouteAlias(value = "info", layout = MainAppLayout.class)
@PageTitle("Информация")
@AnonymousAllowed
@Slf4j
public class InfoView extends VerticalLayout implements BeforeEnterObserver {

    private final SecurityService securityService;

    public InfoView(InfoLayout infoLayout, SecurityService securityService) {
        this.securityService = securityService;
        add(infoLayout);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        securityService.maybeUser().ifPresent(
                user -> log.info("Юзер {} перешел на информационную страницу", user.getName()));
    }
}