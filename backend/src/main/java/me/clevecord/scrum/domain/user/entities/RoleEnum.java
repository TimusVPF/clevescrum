package me.clevecord.scrum.domain.user.entities;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ROLE_USER("ROLE_USER"),
    ROLE_MODERATOR("ROLE_MODERATOR"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_SUPERADMIN("ROLE_SUPERADMIN");

    private final String role;

    RoleEnum(final String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}
