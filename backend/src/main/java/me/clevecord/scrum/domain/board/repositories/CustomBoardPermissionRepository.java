package me.clevecord.scrum.domain.board.repositories;

import me.clevecord.scrum.domain.board.entities.BoardPermissions;

public interface CustomBoardPermissionRepository {

    void createPermission(int boardId, int userId, BoardPermissions permission);
}
