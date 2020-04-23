package me.clevecord.scrum.controllers;

import lombok.AllArgsConstructor;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.domain.user.repositories.UserRepository;
import me.clevecord.scrum.errors.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<User> create(@RequestBody Map<String, String> body)
        throws NoSuchAlgorithmException {
        String username = body.get("username");
        String email = body.get("email");
        String password = body.get("password");
        validateRegistration(username, password, email);

        Date now = new Date();

        User user = User.builder()
            .username(username)
            .email(email)
            .password(encoder.encode(password))
            .enabled(false)
            .createdAt(now)
            .updatedAt(now)
            .build();
        repo.save(user);
        return ResponseEntity.ok(User.builder()
            .username(username)
            .createdAt(now)
            .build());
    }

    private void validateRegistration(String username, String password, String email) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username cannot be empty.");
        }
        if (password == null || password.isBlank()) {
            throw new ValidationException("Password cannot be empty.");
        }
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email cannot be empty.");
        }
        if (repo.existsByUsername(username)) {
            throw new ValidationException("User already exists.");
        }
    }
}
