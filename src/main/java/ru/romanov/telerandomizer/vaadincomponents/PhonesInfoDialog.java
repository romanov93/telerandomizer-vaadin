package ru.romanov.telerandomizer.vaadincomponents;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;

public class PhonesInfoDialog extends Dialog {

    public PhonesInfoDialog() {
        add(infoList());
        getFooter().add(new Button("Закрыть", clickEvent -> close()));
    }

    public UnorderedList infoList() {
        UnorderedList infoList = new UnorderedList();
        infoList.add(
                new ListItem("\"г.о\" - городской округ"),
                new ListItem("\"д\" - деревня"),
                new ListItem("\"ЗАТО\" - закрытое администратиивно-территориальное образование"),
                new ListItem("\"мкр\" - микрорайон"),
                new ListItem("\"м.о.\" - муниципальный округ"),
                new ListItem("\"п\"- поселок"),
                new ListItem("\"пгт\" - поселок городского типа"),
                new ListItem("\"р-н\" - район"),
                new ListItem("\"рп\" - рабочий посёлок"),
                new ListItem("\"с\" - село"),
                new ListItem("\"сл.\" - слобода"),
                new ListItem("\"ст.\" - станица"),
                new ListItem("\"у\"- улус")
        );
        return infoList;
    }
}
