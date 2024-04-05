package ru.romanov.telerandomizer.layouts;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.Getter;
import ru.romanov.telerandomizer.vaadincomponents.UserTable;
import ru.romanov.telerandomizer.views.AdminView;

import static com.vaadin.flow.component.icon.VaadinIcon.CLOSE;

@SpringComponent
@RouteScope
@RouteScopeOwner(AdminView.class)
@Getter
public class AdminLayout extends VerticalLayout {

    private Button leaveAdminMenuButton = new Button("Выйти из админки", new Icon(CLOSE));

    private UserTable userTable = new UserTable();

    private TextField filter = new TextField();


    public AdminLayout() {
        filter.setPlaceholder("Введите имя юзера...");

        add(leaveAdminMenuButton, filter, userTable);
    }
}