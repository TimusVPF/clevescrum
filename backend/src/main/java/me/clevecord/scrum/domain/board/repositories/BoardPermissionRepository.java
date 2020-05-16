package me.clevecord.scrum.domain.board.repositories;

import me.clevecord.scrum.domain.board.entities.BoardPermission;
import me.clevecord.scrum.domain.board.entities.BoardPermissionKey;
import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardPermissionRepository extends CrudRepository<BoardPermission, BoardPermissionKey> {

    @Modifying
    @Query(
        value = "INSERT INTO board_permissions(board_id, user_id, permission) VALUES(:board_id, :user_id, :permission)",
        nativeQuery = true
    )
    void createPermission(@Param("board_id") int boardId, @Param("user_id") int userId,
        @Param("permission") String permission);

    @Modifying
    @Query(
        value = "DELETE FROM board_permissions WHERE board_id=:board_id AND user_id=:user_id AND permission=:permission",
        nativeQuery = true
    )
    void deletePermission(@Param("board_id") int boardId, @Param("user_id") int userId,
        @Param("permission") String permission);
}
