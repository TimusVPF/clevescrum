package me.clevecord.scrum.controllers;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.helpers.ResponseData;
import me.clevecord.scrum.helpers.auth.AuthenticationHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static me.clevecord.scrum.helpers.ResponseData.EMPTY_METADATA;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationHelper authenticationHelper;

    @GetMapping("/user")
    public ResponseEntity<ResponseData<User>> getUser() {
        final User activeUser = authenticationHelper.getAuthenticatedUser();
        return ResponseEntity.ok(new ResponseData<User>(User.builder()
            .id(activeUser.getId())
            .username(activeUser.getUsername())
            .email(activeUser.getEmail())
            .createdAt(activeUser.getCreatedAt())
            .updatedAt(activeUser.getUpdatedAt())
            .enabled(activeUser.isEnabled())
            .build(), EMPTY_METADATA));
    }
}
