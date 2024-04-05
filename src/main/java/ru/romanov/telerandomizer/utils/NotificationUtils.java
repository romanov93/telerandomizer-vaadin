package ru.romanov.telerandomizer.utils;

import com.vaadin.flow.component.notification.Notification;
import lombok.experimental.UtilityClass;

import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_PRIMARY;

@UtilityClass
public class NotificationUtils {

    public static void showNotification(String text) {
        Notification notification = new Notification(text, 3000);
        notification.addThemeVariants(LUMO_PRIMARY);
        notification.open();
    }
}
