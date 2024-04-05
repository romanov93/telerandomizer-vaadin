package ru.romanov.telerandomizer.vaadincomponents;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import lombok.Getter;
import ru.romanov.telerandomizer.entity.User;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;

@Getter
public class DeleteUserDialog extends Dialog {

    private Span questionSpan = new Span("Вы действительно хотите удалить юзера?");

    private Button deleteButton = new Button("Удалить");

    private Button cancelButton = new Button("Отмена", click -> close());

    private User userToDelete;

    public DeleteUserDialog() {
        setHeaderTitle("Удаление пользователя");
        add(questionSpan);
        deleteButton.addThemeVariants(LUMO_PRIMARY, LUMO_ERROR);
        cancelButton.addThemeVariants(LUMO_PRIMARY);
        getFooter().add(deleteButton, cancelButton);
    }

    public void setUserAndOpen(User user) {
        userToDelete = user;
        open();
    }
}