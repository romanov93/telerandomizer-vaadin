package ru.romanov.telerandomizer.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.slf4j.Slf4j;
import ru.romanov.telerandomizer.MainAppLayout;
import ru.romanov.telerandomizer.service.SecurityService;
import ru.romanov.telerandomizer.binders.SearchBinder;
import ru.romanov.telerandomizer.layouts.SearchLayout;

@Route(value = "", layout = MainAppLayout.class)
@RouteAlias(value = "find_numbers", layout = MainAppLayout.class)
@PageTitle("Поиск номеров")
@AnonymousAllowed
@Slf4j
public class SearchView extends VerticalLayout implements BeforeEnterObserver {

    private final SecurityService securityService;

    public SearchView(SearchLayout searchLayout, SearchBinder searchBinder, SecurityService securityService) {
        this.securityService = securityService;
        add(searchLayout);
        searchBinder.addBinding();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        securityService.maybeUser().ifPresent(
                user -> log.info("Юзер {} перешел на страницу поиска номеров", user.getName()));
    }
}