package me.clevecord.scrum.helpers.auth;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import me.clevecord.scrum.domain.user.entities.RoleEnum;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.domain.user.entities.UserRole;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PermissionHelper {

    public boolean isModerator(User user) {
        return userHasAnyRole(user, new HashSet<>() {{
            add(RoleEnum.ROLE_SUPERADMIN);
            add(RoleEnum.ROLE_ADMIN);
            add(RoleEnum.ROLE_MODERATOR);
        }});
    }

    public boolean isAdmin(User user) {
        return userHasAnyRole(user, new HashSet<>() {{
            add(RoleEnum.ROLE_SUPERADMIN);
            add(RoleEnum.ROLE_ADMIN);
        }});
    }

    public boolean isSuperAdmin(User user) {
        return userHasAnyRole(user, new HashSet<>() {{
            add(RoleEnum.ROLE_SUPERADMIN);
        }});
    }

    private boolean userHasAnyRole(User user, Set<RoleEnum> roles) {
        List<UserRole> userRoles = user.getRoles();
        for (UserRole role : userRoles) {
            if (roles.contains(role.getRole())) {
                return true;
            }
        }
        return false;
    }
}
