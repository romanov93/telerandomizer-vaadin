package ru.romanov.telerandomizer.randomizer;

import org.springframework.stereotype.Component;
import ru.romanov.telerandomizer.entity.Phone;
import ru.romanov.telerandomizer.entity.PhoneRange;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.shuffle;

@Component
public class RandomPhonesSearcher extends PhonesSearcher {

    @Override
    public List<Phone> search(UserRequest request) {
        request.fixRangesCapacity();
        shuffle(request.getRangesMatchingRequest());

        List<Phone> newPhones = new ArrayList<>();
        double coefficient = request.findCoefficient();
        double accumulation = 0;
        for (PhoneRange range : request.getRangesMatchingRequest()) {
            double requiredQuantity = range.getCapacity() * coefficient;
            if (requiredQuantity < 1) {
                accumulation += requiredQuantity;
                if (accumulation >= 1) {
                    accumulation -= 1;
                    addPhonesToList(1, newPhones, range, request);
                }
            } else
                addPhonesToList((int)requiredQuantity, newPhones, range, request);
        }
        return newPhones;
    }

    private void addPhonesToList(int howManyShouldGenerate,
                                 List<Phone> newPhones,
                                 PhoneRange range,
                                 UserRequest request) {

        List<Integer> newNumbers = new RandomNumbersGenerator(
                request.findAlreadyUsedNumbersInRange(range.getId()),
                range.getFirstPhone(),
                range.getLastPhone(),
                howManyShouldGenerate
        ).generateRandomInRange();

        newNumbers.forEach(number -> newPhones.add(buildPhone(request, range, number)));
    }
}