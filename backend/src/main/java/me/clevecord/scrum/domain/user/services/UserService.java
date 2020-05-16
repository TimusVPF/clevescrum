package me.clevecord.scrum.domain.user.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.domain.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userInfoRepository;
    private final PasswordEncoder encoder;

    public Optional<User> readById(int id) {
        return userInfoRepository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userInfoRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
            new ArrayList<>());
    }

    public User createUser(String username, String password, String email, boolean enabled) throws UsernameNotFoundException {
        ZonedDateTime now = ZonedDateTime.now();
        User user = User.builder()
            .username(username)
            .password(encoder.encode(password))
            .email(email)
            .enabled(enabled)
            .createdAt(now)
            .updatedAt(now)
            .build();

        userInfoRepository.save(user);
        return User.builder()
            .username(username)
            .password(null)
            .email(email)
            .enabled(enabled)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }
}
