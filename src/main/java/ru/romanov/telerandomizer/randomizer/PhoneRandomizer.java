package ru.romanov.telerandomizer.randomizer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.romanov.telerandomizer.entity.Phone;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Collections.shuffle;
import static ru.romanov.telerandomizer.utils.ListsUtils.cutToSize;

@Component
@RequiredArgsConstructor
public class PhoneRandomizer {

    private final AllPhonesSearcher allPhonesSearcher;
    private final RandomPhonesSearcher randomPhonesSearcher;

    public List<Phone> randomize(UserRequest userRequest) {
        List<Phone> newPhones = searchPhones(userRequest);
        LocalDateTime creationDate = LocalDateTime.now();
        newPhones.forEach(phone -> phone.setCreationDate(creationDate));
        shuffle(newPhones);
        cutToSize(newPhones, userRequest.getRequiredQuantity());
        return newPhones;
    }

    private List<Phone> searchPhones(UserRequest userRequest) {
        if (userRequest.isUserAlreadyHaveTheMostPhones() || userRequest.countNotUsedPhonesMatchingRequest() < 1000)
            return allPhonesSearcher.search(userRequest);
        else
            return randomPhonesSearcher.search(userRequest);
    }
}