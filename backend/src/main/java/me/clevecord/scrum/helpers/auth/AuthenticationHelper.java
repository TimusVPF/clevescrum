package me.clevecord.scrum.helpers.auth;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.domain.user.repositories.UserRepository;
import me.clevecord.scrum.errors.UnauthorizedException;
import me.clevecord.scrum.helpers.jwt.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class AuthenticationHelper {

    private final UserRepository userRepository;
    private final HttpServletRequest request;
    private final JwtTokenUtil jwtTokenUtil;

    public User getAuthenticatedUser() {
        final Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        final String username = authentication.getName();
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UnauthorizedException("User cannot be found.");
        }
        return user;
    }

    public User getAuthenticatedUser(HttpServletRequest request) {
        final Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        String username;
        if (authentication != null) {
            username = authentication.getName();
        } else {
            username = jwtTokenUtil.getUsernameFromToken(jwtTokenUtil.getJwtTokenFromRequest(request));
        }
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UnauthorizedException("User cannot be found.");
        }
        return user;
    }
}
