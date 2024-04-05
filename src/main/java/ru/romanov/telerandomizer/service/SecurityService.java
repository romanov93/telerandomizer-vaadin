package ru.romanov.telerandomizer.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.romanov.telerandomizer.entity.User;
import ru.romanov.telerandomizer.repository.UserRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public Optional<User> maybeUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByName(userName);
    }

    public void logout() {
        authenticationContext.logout();
    }
}
