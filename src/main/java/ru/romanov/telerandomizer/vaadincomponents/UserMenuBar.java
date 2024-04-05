package ru.romanov.telerandomizer.vaadincomponents;


import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import lombok.Getter;

@Getter
public class UserMenuBar extends MenuBar {

    private MenuItem usernameItem = addItem("");

    public UserMenuBar(String username) {
        setThemeName("tertiary-inline contrast");

        Div div = new Div();
        div.add(username);
        div.add(new Icon("lumo", "dropdown"));
        div.getElement().getStyle().set("display", "flex");
        div.getElement().getStyle().set("align-items", "center");
        div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
        usernameItem.add(div);
    }
}
