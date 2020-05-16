package me.clevecord.scrum.domain.comments.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.comments.entities.CardComment;
import me.clevecord.scrum.domain.comments.repositories.CardCommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateCardCommentService {

    private final CardCommentRepository cardCommentRepository;
    private final ReadCardCommentService readCardCommentService;

    public CardComment updateComment(CardComment cardComment, String comment) {
        cardComment.setComment(comment);
        return cardCommentRepository.save(cardComment);
    }

    public void deleteComment(CardComment cardComment) {
        cardCommentRepository.delete(cardComment);
    }
}
