package ru.romanov.telerandomizer.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.RolesAllowed;
import ru.romanov.telerandomizer.binders.AdminBinder;
import ru.romanov.telerandomizer.layouts.AdminLayout;

@Route("admin")
@RouteAlias("admin_users")
@RolesAllowed("ADMIN")
@PageTitle("Управление юзерами")
public class AdminView extends VerticalLayout {

    public AdminView(AdminLayout adminLayout, AdminBinder adminBinder) {
        add(adminLayout);
        adminBinder.addBinding();
    }
}
