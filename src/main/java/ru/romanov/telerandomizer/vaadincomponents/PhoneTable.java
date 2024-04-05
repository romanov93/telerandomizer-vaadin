package ru.romanov.telerandomizer.vaadincomponents;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import lombok.Getter;
import ru.romanov.telerandomizer.dto.PhoneDTO;

import static com.vaadin.flow.component.button.ButtonVariant.*;
import static com.vaadin.flow.component.grid.GridVariant.LUMO_COLUMN_BORDERS;
import static com.vaadin.flow.component.icon.VaadinIcon.EDIT;
import static com.vaadin.flow.theme.lumo.LumoUtility.FontWeight.BOLD;

@Getter
public class PhoneTable extends Grid<PhoneDTO> {

    private Column<PhoneDTO> numberColumn = addComponentColumn(this::numberBoldSpan);
    private Column<PhoneDTO> regionColumn = addColumn(PhoneDTO::getRegionName);
    private Column<PhoneDTO> cityColumn = addColumn(PhoneDTO::getCityName);
    private Column<PhoneDTO> dateColumn = addColumn(PhoneDTO::getSearchDate);
    private Column<PhoneDTO> statusColumn = addComponentColumn(this::statusSpan);
    private Column<PhoneDTO> commentColumn = addColumn(PhoneDTO::getComment);
    private Editor<PhoneDTO> tableEditor = getEditor();
    private Column<PhoneDTO> editColumn = addComponentColumn(this::editButton);
    private Binder<PhoneDTO> tableBinder = new Binder<>(PhoneDTO.class);
    private ComboBox<String> statusComboBox = new ComboBox<>();
    private TextArea commentTextArea = new TextArea();
    private Button saveButton = new Button("Сохранить");
    private Button cancelButton = new Button(VaadinIcon.CLOSE.create(), event -> tableEditor.cancel());
    private HorizontalLayout editButtonsLayout = new HorizontalLayout(saveButton, cancelButton);

    public PhoneTable() {
        setAllRowsVisible(true);
        addThemeVariants(LUMO_COLUMN_BORDERS);
        setHeight("450px");
        configureTableColumns();
        cancelButton.addThemeVariants(LUMO_CONTRAST);
        saveButton.addThemeVariants(LUMO_PRIMARY, LUMO_SUCCESS);
        editButtonsLayout.setPadding(false);
        configureTableEditor();
        bindFieldsWithEditor();
        statusComboBox.setItems("не обработан", "обработан", "перезвонить", "недозвон");
        configureValidationOfCommentField();
        commentTextArea.setPlaceholder("Введите до 50 символов");
    }

    private void configureTableColumns() {
        regionColumn.setHeader("Регион")
                .setSortable(true).setResizable(true)
                .setWidth("200px").setFlexGrow(0);
        cityColumn.setHeader("Населенный пункт")
                .setSortable(true).setResizable(true)
                .setWidth("300px").setFlexGrow(0);
        numberColumn.setHeader("Телефон")
                .setSortable(true).setResizable(true)
                .setWidth("150px").setFlexGrow(0)
                .setComparator(PhoneDTO::getPhoneNumber);
        dateColumn.setHeader("Дата поиска")
                .setSortable(true).setResizable(true)
                .setWidth("150px").setFlexGrow(0);
        statusColumn.setHeader("Статус звонка")
                .setSortable(true).setResizable(true)
                .setWidth("150px").setFlexGrow(0)
                .setComparator(PhoneDTO::getStatus);
        commentColumn.setHeader("Комментарий")
                .setResizable(true)
                .setWidth("300px").setFlexGrow(0);
        editColumn.setHeader("Обработать звонок")
                .setWidth("200px").setFlexGrow(0);
    }

    private Span numberBoldSpan(PhoneDTO phoneDTO) {
        Span span = new Span(String.valueOf(phoneDTO.getPhoneNumber()));
        span.addClassNames(BOLD);
        return span;
    }

    private Span statusSpan(PhoneDTO phoneDTO) {
        Span span = new Span(phoneDTO.getStatus());
        switch (phoneDTO.getStatus()) {
            case "не обработан" -> span.getElement().getThemeList().add("badge");
            case "обработан" -> span.getElement().getThemeList().add("badge success primary");
            case "недозвон" -> span.getElement().getThemeList().add("badge error primary");
            case "перезвонить" -> span.getElement().getThemeList().add("badge primary");
        }
        return span;
    }

    private Button editButton(PhoneDTO phoneDTO) {
        Button editButton = new Button("Обработать",new Icon(EDIT));
        editButton.addThemeVariants(LUMO_SMALL);
        editButton.addClickListener(clickEvent -> {
            if (tableEditor.isOpen()) tableEditor.cancel();
            tableEditor.editItem(phoneDTO);
        });
        return editButton;
    }

    private void configureTableEditor() {
        tableEditor.setBinder(tableBinder);
        // изменения назначаются только после нажатия кнопки save:
        tableEditor.setBuffered(true);
        editColumn.setEditorComponent(editButtonsLayout);
    }

    private void bindFieldsWithEditor() {
        statusComboBox.setWidthFull();
        tableBinder.forField(statusComboBox).bind(PhoneDTO::getStatus, PhoneDTO::setStatus);
        statusColumn.setEditorComponent(statusComboBox);

        commentTextArea.setWidthFull();
        tableBinder.forField(commentTextArea).bind(PhoneDTO::getComment, PhoneDTO::setComment);
        commentColumn.setEditorComponent(commentTextArea);
    }

    public void configureValidationOfCommentField() {
        BeanValidationBinder<PhoneDTO> binder = new BeanValidationBinder<>(PhoneDTO.class);

        binder.forField(commentTextArea).withValidator((Validator<String>) (comment, valueContext) -> {
            if (comment.length() > 100)
                return ValidationResult.error("Используйте не более 100 символов");
            else
                return ValidationResult.ok();
        }).bind(PhoneDTO::getComment, PhoneDTO::setComment);
    }
}

