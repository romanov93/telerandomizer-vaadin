package ru.romanov.telerandomizer.binders;

import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.romanov.telerandomizer.randomizer.UserRequestBuilder;
import ru.romanov.telerandomizer.randomizer.UserRequestHandler;
import ru.romanov.telerandomizer.vaadincomponents.PhonesInfoDialog;
import ru.romanov.telerandomizer.vaadincomponents.RejectedDialog;
import ru.romanov.telerandomizer.entity.*;
import ru.romanov.telerandomizer.randomizer.UserRequest;
import ru.romanov.telerandomizer.service.SecurityService;
import ru.romanov.telerandomizer.service.CityService;
import ru.romanov.telerandomizer.service.RegionService;
import ru.romanov.telerandomizer.layouts.SearchLayout;
import ru.romanov.telerandomizer.views.SearchView;

import java.util.*;
import java.util.stream.Collectors;

import static ru.romanov.telerandomizer.entity.Phone.NUMBER_TYPE_HOME;
import static ru.romanov.telerandomizer.utils.NotificationUtils.showNotification;

@SpringComponent
@RouteScope
@RouteScopeOwner(SearchView.class)
@Slf4j
@RequiredArgsConstructor
public class SearchBinder {

    private final SearchLayout searchLayout;
    private final SecurityService securityService;
    private final RegionService regionService;
    private final CityService cityService;
    private final UserRequestBuilder userRequestBuilder;
    private final UserRequestHandler userRequestHandler;
    private PhonesInfoDialog infoDialog = new PhonesInfoDialog();

    public void addBinding() {
        configurePageOpenEvent();
        configureEventsListeners();
    }

    private void configurePageOpenEvent() {
        searchLayout.getRegionComboBox().setItems(
                (Collection<String>)
                        regionService.findAll()
                                .stream()
                                .map(Region::getName)
                                .collect(Collectors.toCollection(TreeSet::new)));
    }

    private void configureEventsListeners() {
        searchLayout.getInfoButton().addClickListener(click -> infoDialog.open());

        searchLayout.getRegionComboBox().addValueChangeListener(click -> regionChangeAction());

        searchLayout.getTypeChangeRadioButton().addValidationStatusChangeListener(choose -> typeChangeAction());

        searchLayout.getSearchButton().addClickListener(click -> tryToHandleUserRequest());
    }

    private void tryToHandleUserRequest() {
        if (!allFieldsEnteredCorrectly())
            return;

        UserRequest userRequest = userRequestBuilder.build(
                searchLayout.getRegionComboBox().getValue(),
                searchLayout.getCityComboBox().getValue(),
                searchLayout.getQuantityPhonesField().getValue(),
                searchLayout.getTypeChangeRadioButton().getSelectedType(),
                securityService.maybeUser()
        );
        if (userRequest.isRequestedMorePhonesThenExists())
            new RejectedDialog(userRequest.countNotUsedPhonesMatchingRequest()).open();
        else
            userRequestHandler.handle(userRequest);
    }

    private boolean allFieldsEnteredCorrectly() {
        Integer quantity = searchLayout.getQuantityPhonesField().getValue();
        Optional<User> currentUser = securityService.maybeUser();
        if (quantity == null)
            showNotification("Не указано сколько номеров нужно искать.");
        else if (quantity < 1)
            showNotification("Некорректное число.");
        else if (searchLayout.getRegionComboBox().getValue() == null)
            showNotification("Не выбран регион для поиска.");
        else if (searchLayout.getTypeChangeRadioButton().getSelectedType() == NUMBER_TYPE_HOME
                && searchLayout.getCityComboBox().getValue() == null)
            showNotification("Не выбран населенный пункт для поиска.");
        else if (currentUser.isEmpty() && quantity > 100)
            showNotification("Нельзя искать больше 100 номеров без регистрации.");
        else if (currentUser.isPresent()
                && currentUser.get().getRole().equals("USER") && quantity > 1000)
            showNotification("Нельзя искать больше 1000 номеров за один поиск");
        else if (currentUser.isPresent()
                && currentUser.get().getRole().matches("PREMIUM|ADMIN") && quantity > 10000000)
            showNotification("Нельзя искать больше 10000 номеров за один поиск");
        else
            return true;

        return false;
    }

    private void regionChangeAction() {
        String currentRegion = searchLayout.getRegionComboBox().getValue();
        if (currentRegion != null && searchLayout.getTypeChangeRadioButton().getSelectedType() == NUMBER_TYPE_HOME) {
            searchLayout.getCityComboBox().setItems(
                    (Collection<String>)
                            cityService.findAllByRegionName(currentRegion)
                                    .stream()
                                    .map(City::getName)
                                    .collect(Collectors.toCollection(TreeSet::new))
            ).addItem("ВСЕ");
        } else
            searchLayout.getCityComboBox().setItems(Collections.emptyList());
    }

    private void typeChangeAction() {
        switch (searchLayout.getTypeChangeRadioButton().getValue()) {
            case ("Мобильные") -> {
                searchLayout.getCityComboBox().setItems(Collections.emptyList());
                searchLayout.getCityComboBox().setReadOnly(true);
            }
            case ("Домашние") -> {
                searchLayout.getCityComboBox().setReadOnly(false);
                String selectedRegionName = searchLayout.getRegionComboBox().getValue();
                if (selectedRegionName != null)
                    searchLayout.getCityComboBox().setItems(
                            (Collection<String>)
                                    cityService.findAllByRegionName(selectedRegionName)
                                            .stream()
                                            .map(City::getName)
                                            .collect(Collectors.toCollection(TreeSet::new))
                    ).addItem("ВСЕ");
            }
        }
    }
}