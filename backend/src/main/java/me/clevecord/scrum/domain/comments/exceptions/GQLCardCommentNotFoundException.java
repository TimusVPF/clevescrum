package me.clevecord.scrum.domain.comments.exceptions;

import me.clevecord.scrum.errors.graphql.GQLGeneralError;

public class GQLCardCommentNotFoundException extends GQLGeneralError {

    public GQLCardCommentNotFoundException(String message) {
        super(message);
    }
}
