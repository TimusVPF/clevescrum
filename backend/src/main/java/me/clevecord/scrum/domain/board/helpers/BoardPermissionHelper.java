package me.clevecord.scrum.domain.board.helpers;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.board.entities.*;
import me.clevecord.scrum.domain.board.exceptions.GQLInsufficientPermissionException;
import me.clevecord.scrum.domain.category.entities.BoardCategory;
import me.clevecord.scrum.domain.comments.entities.CardComment;
import me.clevecord.scrum.domain.task.entities.Task;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.helpers.auth.PermissionHelper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardPermissionHelper {

    private final PermissionHelper permissionHelper;

    public void assertUserHasPermission(User user, CardComment comment, BoardPermissions permission, boolean strict) {
        if (!userHasPermission(user, comment.getCard(), permission, strict)) {
            throw new GQLInsufficientPermissionException("insufficient permission");
        }
    }

    public void assertUserHasPermission(User user, Task task, BoardPermissions permission, boolean strict) {
        if (!userHasPermission(user, task.getCard(), permission, strict)) {
            throw new GQLInsufficientPermissionException("insufficient permission");
        }
    }

    public void assertUserHasPermission(User user, TaskCard card, BoardPermissions permission, boolean strict) {
        if (!userHasPermission(user, card.getCategory(), permission, strict)) {
            throw new GQLInsufficientPermissionException("insufficient permission");
        }
    }

    public void assertUserHasPermission(User user, BoardCategory category, BoardPermissions permission, boolean strict) {
        if (!userHasPermission(user, category.getBoard(), permission, strict)) {
            throw new GQLInsufficientPermissionException("insufficient permission");
        }
    }

    public void assertUserHasPermission(User user, Board board, BoardPermissions permission, boolean strict) {
        if (!userHasPermission(user, board, permission, strict)) {
            throw new GQLInsufficientPermissionException("insufficient permission");
        }
    }

    public boolean userHasPermission(User user, CardComment comment, BoardPermissions permission, boolean strict) {
        return userHasPermission(user, comment.getCard(), permission, strict);
    }

    public boolean userHasPermission(User user, Task task, BoardPermissions permission, boolean strict) {
        return userHasPermission(user, task.getCard(), permission, strict);
    }

    public boolean userHasPermission(User user, TaskCard card, BoardPermissions permission, boolean strict) {
        return userHasPermission(user, card.getCategory(), permission, strict);
    }

    public boolean userHasPermission(User user, BoardCategory category, BoardPermissions permission, boolean strict) {
        return userHasPermission(user, category.getBoard(), permission, strict);
    }

    public boolean userHasPermission(User user, Board board, BoardPermissions permission, boolean strict) {
        List<BoardPermission> permissions = board.getPermissions();
        boolean permissionFulfilled = false;
        for (BoardPermission permissionItem : permissions) {
            BoardPermissions boardPermission = permissionItem.getKey().getPermission();
            User boardUser = permissionItem.getKey().getUser();
            if (boardPermission == permission && user.getId() == boardUser.getId()) {
                permissionFulfilled = true;
                break;
            }
        }
        if (!(strict || permissionFulfilled)) {
            permissionFulfilled = isUserAdmin(user, board);
        }
        return permissionFulfilled;
    }

    private boolean isUserAdmin(User user, Board board) {
        return permissionHelper.isAdmin(user) ||
            board.getOwner().getId() == user.getId();
    }
}
