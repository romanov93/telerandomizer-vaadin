package ru.romanov.telerandomizer.filewriters;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.server.StreamResource;

import java.util.List;

public interface FileWriter<T> {

    StreamResource streamResource(List<T> items, String fileName);
    default StreamResource streamResource(Grid<T> table, String fileName) {
        return streamResource((List<T>) ((ListDataProvider<T>)table.getDataProvider()).getItems(), fileName);
    }
}