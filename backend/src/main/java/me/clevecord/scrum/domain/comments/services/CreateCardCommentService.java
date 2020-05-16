package me.clevecord.scrum.domain.comments.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.comments.entities.CardComment;
import me.clevecord.scrum.domain.comments.repositories.CardCommentRepository;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import me.clevecord.scrum.errors.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static me.clevecord.scrum.domain.comments.helpers.CardCommentVerificator.validComment;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateCardCommentService {

    private final CardCommentRepository cardCommentRepository;
    private final ReadCardCommentService readCardCommentService;

    public CardComment createComment(TaskCard taskCard, String comment) {
        if (!validComment(comment)) {
            throw new ValidationException("comment cannot be empty");
        }
        final CardComment cardComment = CardComment.builder()
            .card(taskCard)
            .comment(comment)
            .build();
        return cardCommentRepository.save(cardComment);
    }
}
