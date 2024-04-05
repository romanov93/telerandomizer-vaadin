package ru.romanov.telerandomizer.layouts;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.Getter;
import ru.romanov.telerandomizer.vaadincomponents.MyLoginForm;
import ru.romanov.telerandomizer.views.LoginView;

import static com.vaadin.flow.component.button.ButtonVariant.*;

@SpringComponent
@RouteScope
@RouteScopeOwner(LoginView.class)
@Getter
public class LoginLayout extends VerticalLayout {

    private MyLoginForm loginForm = new MyLoginForm();
    private Button registrationButton = new Button("Регистрация");
    private H1 title = new H1("Telerandomizer");


    public LoginLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        registrationButton.addThemeVariants(LUMO_PRIMARY, LUMO_SUCCESS, LUMO_LARGE);
        add(title, loginForm, registrationButton);
    }
}
