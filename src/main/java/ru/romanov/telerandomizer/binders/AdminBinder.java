package ru.romanov.telerandomizer.binders;

import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.romanov.telerandomizer.entity.User;
import ru.romanov.telerandomizer.service.UserService;
import ru.romanov.telerandomizer.layouts.AdminLayout;
import ru.romanov.telerandomizer.views.AdminView;

import static ru.romanov.telerandomizer.utils.PageRouteUtils.MAIN_PAGE_ROUTE;

@SpringComponent
@RouteScope
@RouteScopeOwner(AdminView.class)
@RequiredArgsConstructor
@Slf4j
public class AdminBinder {

    private final AdminLayout adminLayout;
    private final UserService userService;

    public void addBinding() {
        configurePageOpenEvent();
        configureEventsListeners();
    }

    private void configurePageOpenEvent() {
        adminLayout.getUserTable().setItems(userService.findAll());
        configureFilter();
    }

    private void configureEventsListeners() {
        adminLayout.getUserTable().getSaveButton().addClickListener(clickEvent -> saveEditingUser());

        adminLayout.getUserTable().getDeleteUserDialog().getDeleteButton().addClickListener(clickEvent -> deleteUser());

        adminLayout.getLeaveAdminMenuButton().addClickListener(event ->
                adminLayout.getLeaveAdminMenuButton().getUI().ifPresent(ui -> ui.navigate(MAIN_PAGE_ROUTE)));
    }

    private void saveEditingUser() {
        User currentlyEditedUser = adminLayout.getUserTable().getTableEditor().getItem();
        String role = adminLayout.getUserTable().getRolesComboBox().getValue();
        currentlyEditedUser.setRole(role);
        currentlyEditedUser.setEmail(adminLayout.getUserTable().getEmailField().getValue());
        currentlyEditedUser.setPassword(adminLayout.getUserTable().getPasswordField().getValue());

        adminLayout.getUserTable().getTableEditor().save();
        userService.update(currentlyEditedUser);
        log.info("Админ отредактировал юзера {}, роль: {}",
                currentlyEditedUser.getName(),
                currentlyEditedUser.getRole()
        );
    }

    private void configureFilter() {
        adminLayout.getFilter().setValueChangeMode(ValueChangeMode.LAZY);
        adminLayout.getFilter().addValueChangeListener(field -> showUsers(field.getValue()));
    }

    private void showUsers(String name) {
        if (name.isEmpty())
            adminLayout.getUserTable().setItems(userService.findAll());
        else
            adminLayout.getUserTable().setItems(userService.findByNameStartingWith(name));
    }

    private void deleteUser() {
        User userToDelete = adminLayout.getUserTable().getDeleteUserDialog().getUserToDelete();

        userService.delete(userToDelete);
        adminLayout.getUserTable().getListDataView().removeItem(userToDelete);
        adminLayout.getUserTable().getDeleteUserDialog().close();
        log.info("Админ удалил юзера {}", userToDelete.getName());
    }
}