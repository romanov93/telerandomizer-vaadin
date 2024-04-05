package ru.romanov.telerandomizer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.romanov.telerandomizer.entity.User;
import ru.romanov.telerandomizer.repository.UserRepository;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> maybeUser = userRepository.findByName(username);
        if (maybeUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        } else
            return new UserPrincipal(maybeUser.get());
    }
}