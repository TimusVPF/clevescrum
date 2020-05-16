package me.clevecord.scrum.domain.comments.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.comments.entities.CardComment;
import me.clevecord.scrum.domain.comments.repositories.CardCommentRepository;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadCardCommentService {

    private final CardCommentRepository repository;

    public Optional<CardComment> findById(int id) {
        Optional<CardComment> cardCommentOptional = repository.findById(id);
        if (cardCommentOptional.isPresent()) {
            CardComment cardComment = cardCommentOptional.get();
            Hibernate.initialize(cardComment.getCard());
            Hibernate.initialize(cardComment.getCard().getCategory());
            Hibernate.initialize(cardComment.getCard().getCategory().getBoard());
        }
        return cardCommentOptional;
    }

    public List<CardComment> getByTaskCardId(int taskCardId) {
        return repository.getByTaskCardId(taskCardId);
    }
}
