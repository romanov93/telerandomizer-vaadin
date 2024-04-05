package ru.romanov.telerandomizer.layouts;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;
import ru.romanov.telerandomizer.views.InfoView;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_CONTRAST;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_SMALL;
import static com.vaadin.flow.component.icon.VaadinIcon.PAPERPLANE_O;
import static com.vaadin.flow.theme.lumo.LumoUtility.FontWeight.MEDIUM;

@SpringComponent
@RouteScope
@RouteScopeOwner(InfoView.class)
public class InfoLayout extends VerticalLayout {

    private final UnorderedList infoUnorderedList = new UnorderedList();
    private final Paragraph info = new Paragraph("Наша почта:");
    private final Paragraph email = new Paragraph("telerandomizer@gmail.com");
    private final Paragraph info2 = new Paragraph("Телеграм:");
    private final Anchor telegram = new Anchor();
    private final HorizontalLayout emailLayout = new HorizontalLayout(info, email);
    private final HorizontalLayout telegramLayout = new HorizontalLayout(info2, telegram);

    public InfoLayout() {
        configureInfoList();
        email.addClassNames(MEDIUM);
        configureTelegramButton();

        add(infoUnorderedList);
    }
    public void configureInfoList() {
        infoUnorderedList.add(
                new ListItem("TeleRandomizer – помощник в проведении телефонных опросов."),
                new ListItem("Здесь вы можете построить базу случайных телефонных номеров по нужному вам региону и городу."),
                new ListItem("Также сервис позволяет делать пометки во время обзвона в таблице на сайте."),
                new ListItem("Для зарегистрированных пользователей сохраняются найденные номера, " +
                        "после они не генерируются повторно."),
                new ListItem("К найденным номерам всегда можно вернуться и скачать таблицу с ними в формате xlsx и csv"),
                new ListItem("Для поиска номеров используется реестр открытых данных Министерства цифрового развития " +
                        "Российской Федерации:"),
                new Anchor("https://opendata.digital.gov.ru/registry/numeric",
                        "https://opendata.digital.gov.ru/registry/numeric"),
                new ListItem("Отбор телефонов производится случайным образом " +
                        "и обеспечивает равномерное распределение по диапазонам номеров."),

                emailLayout,
                telegramLayout
        );
    }

    public void configureTelegramButton() {
        Button telegramButton = new Button("Перейти в телеграм", new Icon(PAPERPLANE_O));
        telegramButton.addThemeVariants(LUMO_CONTRAST, LUMO_SMALL);
        telegram.add(telegramButton);
        telegram.setHref("https://t.me/+dNNWef-jwRRmYzJi");
    }
}