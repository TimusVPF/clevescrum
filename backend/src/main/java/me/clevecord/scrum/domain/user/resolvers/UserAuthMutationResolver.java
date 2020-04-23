package me.clevecord.scrum.domain.user.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.domain.user.repositories.UserRepository;
import me.clevecord.scrum.domain.user.services.UserService;
import me.clevecord.scrum.errors.graphql.GQLValidationException;
import me.clevecord.scrum.helpers.jwt.JwtResponse;
import me.clevecord.scrum.helpers.jwt.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class UserAuthMutationResolver implements GraphQLMutationResolver {

    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository repo;
    private final UserService userService;

    public CompletableFuture<JwtResponse> login(final String username, final String password) throws Exception {
        authenticate(username, password);
        return CompletableFuture.supplyAsync(() -> {
            final UserDetails userDetails = userService
                .loadUserByUsername(username);
            final String token = jwtTokenUtil.generateToken(userDetails);
            return new JwtResponse(token);
        });
    }

    public CompletableFuture<User> register(final String username, final String email, final String password) throws Exception {
        validateRegistration(username, password, email);
        return CompletableFuture.supplyAsync(() -> {
            Date now = new Date();

            User user = User.builder()
                .username(username)
                .email(email)
                .password(encoder.encode(password))
                .enabled(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
            user = repo.save(user);
            return User.builder()
                .id(user.getId())
                .username(username)
                .email(email)
                .createdAt(now)
                .updatedAt(now)
                .build();
        });
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    private void validateRegistration(String username, String password, String email) {
        if (username.isBlank()) {
            throw new GQLValidationException("Username cannot be empty.");
        }
        if (password.isBlank()) {
            throw new GQLValidationException("Password cannot be empty.");
        }
        if (email.isBlank()) {
            throw new GQLValidationException("Email cannot be empty.");
        }
        if (repo.existsByUsername(username)) {
            throw new GQLValidationException("User already exists.");
        }
    }

}
