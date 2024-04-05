package ru.romanov.telerandomizer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.romanov.telerandomizer.entity.Phone;
import ru.romanov.telerandomizer.entity.User;
import ru.romanov.telerandomizer.randomizer.PhoneRandomizer;
import ru.romanov.telerandomizer.randomizer.UserRequest;
import ru.romanov.telerandomizer.repository.PhoneRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PhoneService {
    private final PhoneRepository phoneRepository;
    private final PhoneRandomizer phoneRandomizer;

    @Async
    public CompletableFuture<List<Phone>> generateRandomPhones(UserRequest userRequest) {
        List<Phone> phonesByRequest = phoneRandomizer.randomize(userRequest);
        return CompletableFuture.completedFuture(phonesByRequest);
    }

    public Optional<Phone> findByUserAndNumber(User user, long number) {
        return phoneRepository.findByUserAndNumber(user, number);
    }

    public List<Phone> findAllByUserId(User user) {
        return phoneRepository.findAllByUser(user);
    }

    public void saveAll(List<Phone> phones) {
        phoneRepository.saveAll(phones);
    }

    public void update(Phone phone) {
        phoneRepository.save(phone);
    }

    public List<Phone> findAllByUserAndRegionAndType(User user, String regionName, int numbersType) {
        return phoneRepository.findAllByUserAndRegionNameAndNumberType(user, regionName, numbersType);
    }

    public List<Phone> findAllByUserAndRegionAndCityAndType(User user, String regionName, String city, int numbersType) {
        return phoneRepository.findAllByUserAndRegionNameAndCityNameAndNumberType(user, regionName, city, numbersType);
    }
}