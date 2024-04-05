package ru.romanov.telerandomizer.randomizer;

import ru.romanov.telerandomizer.entity.Phone;
import ru.romanov.telerandomizer.entity.PhoneRange;

import java.util.List;
import java.util.Optional;

import static ru.romanov.telerandomizer.utils.PhoneNumberUtils.buildFullNumber;

public abstract class PhonesSearcher {

    private static final String CALL_STATUS = "не обработан";

    public abstract List<Phone> search(UserRequest userRequest);

    protected Phone buildPhone(UserRequest request, PhoneRange range, int number) {
        Phone phone = Phone.builder()
                .number(buildFullNumber(range.getCode(), number))
                .regionName(range.getRegion().getName())
                .range(range)
                .numberType(request.getRequiredPhoneType())
                .status(CALL_STATUS)
                .build();
        request.getMaybeUser().ifPresent(phone::setUser);
        Optional.ofNullable(range.getCity()).ifPresent(city -> phone.setCityName(city.getName()));
        return phone;
    }
}