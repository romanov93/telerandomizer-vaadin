package ru.romanov.telerandomizer.vaadincomponents;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;
import ru.romanov.telerandomizer.entity.User;

import java.util.List;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ICON;
import static com.vaadin.flow.component.grid.GridVariant.LUMO_COLUMN_BORDERS;

@Getter
public class UserTable extends Grid<User> {

    private Editor<User> tableEditor = getEditor();
    private Binder<User> binder = new Binder<>(User.class);
    private Column<User> idColumn = addColumn(User::getId);
    private Column<User> nameColumn = addColumn(User::getName);
    private Column<User> roleColumn = addColumn(User::getRole);
    private Column<User> emailColumn = addColumn(User::getEmail);
    private Column<User> passwordColumn = addColumn(User::getPassword);
    private Column<User> editColumn = addComponentColumn(this::editButton);
    private Column<User> deleteColumn = addComponentColumn(this::deleteButton);
    private ComboBox<String> rolesComboBox = new ComboBox<>();
    private EmailField emailField = new EmailField();
    private TextField passwordField = new TextField();
    private Button saveButton = new Button("Сохранить");
    private Button cancelButton = new Button(VaadinIcon.CLOSE.create(), event -> tableEditor.cancel());
    private HorizontalLayout editButtonsLayout = new HorizontalLayout(saveButton, cancelButton);
    private DeleteUserDialog deleteUserDialog = new DeleteUserDialog();

    public UserTable() {
        addThemeVariants(LUMO_COLUMN_BORDERS);
        cancelButton.addThemeVariants(LUMO_ICON, LUMO_ERROR);
        editButtonsLayout.setPadding(false);
        setAllRowsVisible(true);
        rolesComboBox.setItems(List.of("USER", "BAN", "ADMIN", "PREMIUM"));
        configureColumns();
        configureTableEditor();
        bindFieldsWithEditor();
    }

    private Button editButton(User user) {
        Button editButton = new Button("Редактировать");
        editButton.addClickListener(click -> {
            if (tableEditor.isOpen())
                tableEditor.cancel();
            if (user.getName().equals("Dimon"))
                Notification.show("Нельзя редактировать админа");
            else
                tableEditor.editItem(user);
        });
        return editButton;
    }

    private Button deleteButton(User user) {
        Button deleteButton = new Button("Удалить");
        deleteButton.addThemeVariants(LUMO_ERROR);
        deleteButton.addClickListener(event -> {
            if (!user.getName().equals("Dimon"))
                deleteUserDialog.setUserAndOpen(user);
        });
        return deleteButton;
    }

    private void configureColumns() {
        idColumn.setHeader("Id")
                .setSortable(true).setResizable(true)
                .setWidth("100px").setFlexGrow(0);
        nameColumn.setHeader("Имя")
                .setSortable(true).setResizable(true)
                .setWidth("250px").setFlexGrow(0);
        roleColumn.setHeader("Роль")
                .setSortable(true).setResizable(true)
                .setWidth("150px").setFlexGrow(0);
        emailColumn.setHeader("Email")
                .setResizable(true)
                .setWidth("250px").setFlexGrow(0);
        passwordColumn.setHeader("Пароль")
                .setResizable(true)
                .setWidth("250px").setFlexGrow(0);
        editColumn.setWidth("200px").setFlexGrow(0);
        deleteColumn.setWidth("200px").setFlexGrow(0);
    }

    private void configureTableEditor() {
        tableEditor.setBinder(binder);
        // изменения назначаются только после нажатия кнопки save:
        tableEditor.setBuffered(true);
        editColumn.setEditorComponent(editButtonsLayout);
    }

    private void bindFieldsWithEditor() {
        rolesComboBox.setWidthFull();
        binder.forField(rolesComboBox)
                .bind(User::getRole, User::setRole);
        roleColumn.setEditorComponent(rolesComboBox);

        emailField.setWidthFull();
        binder.forField(emailField)
                .bind(User::getEmail, User::setEmail);
        emailColumn.setEditorComponent(emailField);

        passwordField.setWidthFull();
        binder.forField(passwordField)
                .bind(User::getPassword, User::setPassword);
        passwordColumn.setEditorComponent(passwordField);
    }
}
