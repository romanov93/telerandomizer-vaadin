package ru.romanov.telerandomizer.randomizer;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class RandomNumbersGenerator {

    private final Set<Integer> unwantedNumbers;
    private final int firstNumber;
    private final int lastNumber;
    private final int requiredQuantity;

    public List<Integer> generateRandomInRange() {
        List<Integer> randomNumbers = new ArrayList<>();

        PrimitiveIterator.OfInt iterator =
                ThreadLocalRandom.current().ints(firstNumber, lastNumber).distinct().iterator();

        while (randomNumbers.size() < requiredQuantity) {
            int number = iterator.nextInt();
            if(!unwantedNumbers.contains(number))
                randomNumbers.add(number);
        }

        return randomNumbers;
    }
}
