package ru.romanov.telerandomizer.vaadincomponents;

import com.vaadin.flow.component.grid.Grid;
import ru.romanov.telerandomizer.dto.PhoneDTO;

import static com.vaadin.flow.component.grid.GridVariant.LUMO_COLUMN_BORDERS;

public class ResultTable extends Grid<PhoneDTO> {

    public ResultTable() {
        configureBeanType(PhoneDTO.class, false);
        addThemeVariants(LUMO_COLUMN_BORDERS);

        Column<PhoneDTO> numberColumn = addColumn(PhoneDTO::getPhoneNumber);
        Column<PhoneDTO> regionColumn = addColumn(PhoneDTO::getRegionName);
        Column<PhoneDTO> cityColumn = addColumn(PhoneDTO::getCityName);

        regionColumn.setHeader("Регион")
                .setSortable(true).setResizable(true)
                .setWidth("200px").setFlexGrow(0);
        cityColumn.setHeader("Населенный пункт")
                .setSortable(true).setResizable(true)
                .setWidth("200px").setFlexGrow(0);
        numberColumn.setHeader("Телефон")
                .setSortable(true).setResizable(false)
                .setWidth("150px").setFlexGrow(0);
    }
}
