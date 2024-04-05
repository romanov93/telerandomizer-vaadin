package ru.romanov.telerandomizer.vaadincomponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MenuItemInfo extends ListItem {

    private final Class<? extends Component> view;
    private RouterLink link = new RouterLink();


    public MenuItemInfo(String menuTitle, Component icon, Class<? extends Component> view) {
        this.view = view;

        link.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.Gap.XSMALL,
                LumoUtility.Height.MEDIUM,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.Padding.Horizontal.SMALL,
                LumoUtility.TextColor.BODY);

        link.setRoute(view);

        Span text = new Span(menuTitle);
        text.addClassNames(
                LumoUtility.FontWeight.MEDIUM,
                LumoUtility.FontSize.MEDIUM,
                LumoUtility.Whitespace.NOWRAP);

        link.add(text);

        if (icon != null)
            link.add(icon);

        add(link);
    }

    public Class<?> getView() {
        return view;
    }

}