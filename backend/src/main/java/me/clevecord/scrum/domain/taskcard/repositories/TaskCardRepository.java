package me.clevecord.scrum.domain.taskcard.repositories;

import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskCardRepository extends CrudRepository<TaskCard, Integer> {

    @Query("SELECT taskCard FROM TaskCard taskCard WHERE taskCard.category.id = ?1")
    List<TaskCard> findByCategoryId(int categoryId);

    @Modifying
    @Query(value = "INSERT INTO card_participants (user_id, card_id) VALUES (:user_id, :card_id)", nativeQuery = true)
    void joinCard(@Param("user_id") int userId, @Param("card_id") int taskCardId);

    @Modifying
    @Query(value = "DELETE FROM task_cards WHERE id=:id", nativeQuery = true)
    void delete(@Param("id") int taskCardId);
}
