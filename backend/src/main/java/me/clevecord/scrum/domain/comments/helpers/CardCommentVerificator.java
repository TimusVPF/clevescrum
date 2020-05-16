package me.clevecord.scrum.domain.comments.helpers;

public class CardCommentVerificator {

    public static boolean validComment(String comment) {
        return !comment.isBlank();
    }
}
