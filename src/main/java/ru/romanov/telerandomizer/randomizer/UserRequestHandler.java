package ru.romanov.telerandomizer.randomizer;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.romanov.telerandomizer.dto.PhoneDTO;
import ru.romanov.telerandomizer.filewriters.CsvWriter;
import ru.romanov.telerandomizer.filewriters.XlsxWriter;
import ru.romanov.telerandomizer.vaadincomponents.FailureDialog;
import ru.romanov.telerandomizer.vaadincomponents.ResultDialog;
import ru.romanov.telerandomizer.vaadincomponents.WaitingDialog;
import ru.romanov.telerandomizer.entity.Phone;
import ru.romanov.telerandomizer.mappers.PhoneListMapper;
import ru.romanov.telerandomizer.service.PhoneService;

import java.util.List;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;

@SpringComponent
@UIScope
@RequiredArgsConstructor
@Slf4j
public class UserRequestHandler {
    private final XlsxWriter xlsxWriter;
    private final CsvWriter csvWriter;
    private final PhoneService phoneService;
    private final PhoneListMapper phoneListMapper;
    private WaitingDialog waitingDialog = new WaitingDialog();
    private UserRequest userRequest;

    public void handle(UserRequest userRequest) {
        this.userRequest = userRequest;
        waitingDialog.open();
        logBefore();
        UI ui = UI.getCurrent();
        ui.getLoadingIndicatorConfiguration().setApplyDefaultTheme(false);
        phoneService
                .generateRandomPhones(userRequest)
                .orTimeout(30, SECONDS)
                .whenCompleteAsync((requiredPhones, throwable) -> {
                    if (throwable != null)
                        ui.access(() -> failureAction(throwable));
                    else
                        ui.access(() -> successAction(requiredPhones));
                });
    }

    @SneakyThrows
    private void failureAction(Throwable throwable) {
        waitingDialog.close();
        logFailure();
        new FailureDialog().open();
        throw throwable;
    }

    @SneakyThrows
    private void successAction(List<Phone> newPhones) {
        sleep(2000);
        userRequest.getMaybeUser().ifPresent(user -> phoneService.saveAll(newPhones));
        waitingDialog.close();
        logSuccess(newPhones.size());

        List<PhoneDTO> newPhonesDTO = phoneListMapper.toDTOList(newPhones);

        new ResultDialog(newPhonesDTO, xlsxWriter, csvWriter, userRequest).open();
    }

    private void logBefore() {
        userRequest.getMaybeUser().ifPresentOrElse(
                user ->
                        log.info("Юзер {}  запросил {} телефонов по региону: {}, по городу: {}",
                                user.getName(),
                                userRequest.getRequiredQuantity(),
                                userRequest.getRequiredRegionName(),
                                userRequest.getRequiredCityName()),
                () ->
                        log.info("Гость запросил {} телефонов по региону: {}, по городу: {}",
                                userRequest.getRequiredQuantity(),
                                userRequest.getRequiredRegionName(),
                                userRequest.getRequiredCityName())
        );
    }

    private void logFailure() {
        userRequest.getMaybeUser().ifPresentOrElse(
                user -> log.info("Для юзера {} не выполнен запрос", user.getName()),
                () -> log.info("Для гостя не выполнен запрос")
        );
    }

    private void logSuccess(int numbersFound) {
        userRequest.getMaybeUser().ifPresentOrElse(
                user -> log.info("Для юзера {} сгенерировалось {} номеров",
                        user.getName(),
                        numbersFound),
                () -> log.info("Для гостя сгенерировалось {} номеров",
                        numbersFound)
        );
    }
}