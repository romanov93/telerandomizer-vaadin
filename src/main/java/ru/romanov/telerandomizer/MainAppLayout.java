package ru.romanov.telerandomizer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.extern.slf4j.Slf4j;
import ru.romanov.telerandomizer.entity.User;
import ru.romanov.telerandomizer.service.SecurityService;
import ru.romanov.telerandomizer.vaadincomponents.LoginDialog;
import ru.romanov.telerandomizer.vaadincomponents.MenuItemInfo;
import ru.romanov.telerandomizer.vaadincomponents.UserMenuBar;
import ru.romanov.telerandomizer.views.InfoView;
import ru.romanov.telerandomizer.views.NumbersView;
import ru.romanov.telerandomizer.views.SearchView;

import java.util.Optional;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_CONTRAST;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY;
import static com.vaadin.flow.component.icon.VaadinIcon.*;

@AnonymousAllowed
@Slf4j
public class MainAppLayout extends AppLayout {

    private final SecurityService securityService;
    private Image appLogo = new Image("images/mylogo.png", "TeleRandomizer");

    public MainAppLayout(SecurityService securityService) {
        this.securityService = securityService;
        addToNavbar(createHeaderContent());
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames(
                LumoUtility.BoxSizing.BORDER,
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Width.FULL);

        Div layout = new Div();
        layout.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.Padding.Horizontal.LARGE);


        appLogo.setWidth("200px");
        appLogo.addClassNames(
                LumoUtility.Margin.Vertical.MEDIUM,
                LumoUtility.Margin.End.AUTO,
                LumoUtility.FontSize.XLARGE,
                LumoUtility.FontWeight.BLACK);

        layout.add(appLogo);

        Optional<User> maybeUser = securityService.maybeUser();
        if (maybeUser.isPresent()) {
            UserMenuBar userMenu = new UserMenuBar(maybeUser.get().getName());
            userMenu.getUsernameItem().getSubMenu().addItem("Выйти", event -> {
                log.info("Юзер {} вышел", securityService.maybeUser().get().getName());
                securityService.logout();
            });
            layout.add(userMenu);
        } else {
            Button loginButton = new Button("Вход | Регистрация");
            loginButton.addThemeVariants(LUMO_TERTIARY, LUMO_CONTRAST);
            loginButton.addClickListener(click -> new LoginDialog().open());
            layout.add(loginButton);
        }

        Nav nav = new Nav();
        nav.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.Overflow.AUTO,
                LumoUtility.Padding.Horizontal.MEDIUM,
                LumoUtility.Padding.Vertical.XSMALL);

        UnorderedList list = new UnorderedList();
        list.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.Gap.SMALL,
                LumoUtility.ListStyleType.NONE,
                LumoUtility.Margin.NONE,
                LumoUtility.Padding.NONE);

        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems())
            list.add(menuItem);

        header.add(layout, nav);
        return header;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{
                new MenuItemInfo("Поиск новых номеров", new Icon(SEARCH), SearchView.class),
                new MenuItemInfo("Смотреть мои номера", new Icon(TABLE), NumbersView.class),
                new MenuItemInfo("Информация", new Icon(INFO_CIRCLE_O), InfoView.class),
        };
    }
}
