package ru.romanov.telerandomizer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.romanov.telerandomizer.entity.User;
import ru.romanov.telerandomizer.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByNameStartingWith(String username) {
        return userRepository.findByNameStartingWith(username);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}
