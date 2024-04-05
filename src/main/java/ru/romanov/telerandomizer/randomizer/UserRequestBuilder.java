package ru.romanov.telerandomizer.randomizer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.romanov.telerandomizer.entity.Phone;
import ru.romanov.telerandomizer.entity.PhoneRange;
import ru.romanov.telerandomizer.entity.User;
import ru.romanov.telerandomizer.service.PhoneRangeService;
import ru.romanov.telerandomizer.service.PhoneService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.romanov.telerandomizer.entity.Phone.NUMBER_TYPE_MOBILE;

@Component
@RequiredArgsConstructor
public class UserRequestBuilder {

    private final PhoneService phoneService;
    private final PhoneRangeService phoneRangeService;


    public UserRequest build(String regionName,
                              String cityName,
                              int quantity,
                              int numbersType,
                              Optional<User> maybeUser) {

        List<PhoneRange> ranges = findAllRangesMatchingRequest(regionName, cityName, numbersType);
        List<Phone> userPhones = findPhonesThatMatchingRequestAndUserAlreadyHave
                (maybeUser, numbersType, regionName, cityName);

        return UserRequest.builder()
                .maybeUser(maybeUser)
                .requiredRegionName(regionName)
                .requiredCityName(cityName)
                .requiredQuantity(quantity)
                .requiredPhoneType(numbersType)
                .rangesMatchingRequest(ranges)
                .phonesUserAlreadyHave(userPhones)
                .build();
    }

    private List<PhoneRange> findAllRangesMatchingRequest(String regionName,
                                                          String cityName,
                                                          int numbersType) {
        if (numbersType == NUMBER_TYPE_MOBILE || cityName.equals("ВСЕ"))
            return phoneRangeService.findAllByRegionAndNumbersType(regionName, numbersType);
        else
            return phoneRangeService.findAllByRegionAndCityAndNumbersType(regionName, cityName, numbersType);
    }

    private List<Phone> findPhonesThatMatchingRequestAndUserAlreadyHave(Optional<User> maybeUser,
                                                                        int numbersType,
                                                                        String regionName,
                                                                        String cityName) {
        if (maybeUser.isEmpty())
            return Collections.emptyList();
        else if (numbersType == NUMBER_TYPE_MOBILE || cityName.equals("ВСЕ"))
            return phoneService.findAllByUserAndRegionAndType(maybeUser.get(), regionName, numbersType);
        else
            return phoneService.findAllByUserAndRegionAndCityAndType(maybeUser.get(), regionName, cityName, numbersType);
    }
}