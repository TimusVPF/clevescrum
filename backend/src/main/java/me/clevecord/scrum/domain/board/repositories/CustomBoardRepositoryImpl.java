package me.clevecord.scrum.domain.board.repositories;

import me.clevecord.scrum.domain.board.entities.Board;
import me.clevecord.scrum.domain.board.entities.BoardPermission;
import me.clevecord.scrum.domain.user.entities.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Board> getAllBoardsByUserIdAndPermission(int userId, String permission) {
        Query query = entityManager.createNativeQuery(
            "SELECT * from boards where id in (" +
                    "SELECT board_id FROM board_permissions " +
                    "WHERE permission = '" + permission + "' " +
                    "AND user_id = ? " +
                ");", BoardPermission.class
        );
        query.setParameter(1, userId);

        return query.getResultList();
    }
}
