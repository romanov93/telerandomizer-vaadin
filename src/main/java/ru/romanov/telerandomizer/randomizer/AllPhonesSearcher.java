package ru.romanov.telerandomizer.randomizer;

import org.springframework.stereotype.Component;
import ru.romanov.telerandomizer.entity.Phone;
import ru.romanov.telerandomizer.entity.PhoneRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Collections.shuffle;

@Component
public class AllPhonesSearcher extends PhonesSearcher {

    @Override
    public List<Phone> search(UserRequest request) {
        List<Phone> phones = new ArrayList<>();
        shuffle(request.getRangesMatchingRequest());
        request.getRangesMatchingRequest().forEach(range -> addPhonesToList(phones, request, range));

        return phones;
    }

    private void addPhonesToList(List<Phone> phones, UserRequest request, PhoneRange range) {
        Set<Integer> alreadyUsedPhones = request.findAlreadyUsedNumbersInRange(range.getId());

        for (int number = range.getFirstPhone() ; number <= range.getLastPhone() ; number++) {
            if (!alreadyUsedPhones.contains(number)) {
                Phone phone = buildPhone(request, range, number);
                phones.add(phone);
            }
        }
    }
}