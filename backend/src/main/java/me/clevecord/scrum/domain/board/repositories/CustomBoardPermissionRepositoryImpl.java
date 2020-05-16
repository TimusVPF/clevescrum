package me.clevecord.scrum.domain.board.repositories;

import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.PreparedStatement;

@Repository
public class CustomBoardPermissionRepositoryImpl implements CustomBoardPermissionRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void createPermission(int boardId, int userId, BoardPermissions permission) {
        Query query = entityManager.createNativeQuery(
            "INSERT INTO board_permissions (board_id, user_id, permission)" +
            "VALUES (?, ?, ?)"
        );
        query.setParameter(1, boardId);
        query.setParameter(2, userId);
        query.setParameter(3, permission.toString());
        query.executeUpdate();
    }
}
