package ru.romanov.telerandomizer.randomizer;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.romanov.telerandomizer.entity.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@RequiredArgsConstructor
public class UserRequest {

    private final Optional<User> maybeUser;
    private final String requiredRegionName;
    private final String requiredCityName;
    private final int requiredQuantity;
    private final int requiredPhoneType;
    private final List<Phone> phonesUserAlreadyHave;
    private final List<PhoneRange> rangesMatchingRequest;

    public int countNotUsedPhonesMatchingRequest() {
        return countAllPhonesMatchingRequest() - phonesUserAlreadyHave.size();
    }

    public boolean isRequestedMorePhonesThenExists(){
        return requiredQuantity > countNotUsedPhonesMatchingRequest();
    }

    public boolean isUserAlreadyHaveTheMostPhones() {
        return (double) requiredQuantity / countNotUsedPhonesMatchingRequest() > 0.8;
    }

    public double findCoefficient() {
        if (requiredQuantity < 800)
            return 1000.0 / countAllPhonesMatchingRequest();
        else
            return (requiredQuantity * 1.2) / countAllPhonesMatchingRequest();
    }

    public Set<Integer> findAlreadyUsedNumbersInRange(int rangeId) {
        return phonesUserAlreadyHave
                .stream()
                .filter(phone -> phone.getRange().getId() == rangeId)
                .map(Phone::getNumberWithoutCode)
                .collect(Collectors.toSet());
    }

    private int countAllPhonesMatchingRequest() {
        return rangesMatchingRequest.stream().mapToInt(PhoneRange::getCapacity).sum();
    }

    void fixRangesCapacity() {
        if (phonesUserAlreadyHave.isEmpty())
            return;
        List<Integer> rangesId = phonesUserAlreadyHave.stream().map(Phone::getRange).map(PhoneRange::getId).toList();
        for (PhoneRange range : rangesMatchingRequest) {
            int userAlreadyHaveNumbersInCurrentRange = (int) rangesId.stream().filter(id -> id == range.getId()).count();
            range.setCapacity(range.getCapacity() - userAlreadyHaveNumbersInCurrentRange);
        }
    }
}