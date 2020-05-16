package me.clevecord.scrum.domain.comments.repositories;

import me.clevecord.scrum.domain.comments.entities.CardComment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CardCommentRepository extends CrudRepository<CardComment, Integer> {

    @Query(value = "SELECT * FROM card_comments WHERE card_id = :taskCardId", nativeQuery = true)
    List<CardComment> getByTaskCardId(int taskCardId);
}
