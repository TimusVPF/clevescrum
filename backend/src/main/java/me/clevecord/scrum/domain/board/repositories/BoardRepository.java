package me.clevecord.scrum.domain.board.repositories;

import me.clevecord.scrum.domain.board.entities.Board;
import me.clevecord.scrum.domain.user.entities.User;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends CrudRepository<Board, Integer>, CustomBoardRepository {

    @Query(
        value = "SELECT * FROM boards WHERE owner = :id",
        nativeQuery = true
    )
    List<Board> findByOwnerId(int id);
}
