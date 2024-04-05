package ru.romanov.telerandomizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.telerandomizer.entity.Phone;
import ru.romanov.telerandomizer.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Integer> {

    List<Phone> findAllByUser(User user);

    Optional<Phone> findByUserAndNumber(User user, long number);

    List<Phone> findAllByUserAndRegionNameAndNumberType(User user, String regionName, int type);

    List<Phone> findAllByUserAndRegionNameAndCityNameAndNumberType(User user, String regionName, String cityName, int type);
}
