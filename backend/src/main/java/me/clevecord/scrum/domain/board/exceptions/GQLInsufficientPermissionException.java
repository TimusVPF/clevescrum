package me.clevecord.scrum.domain.board.exceptions;

import me.clevecord.scrum.errors.graphql.GQLGeneralError;

public class GQLInsufficientPermissionException extends GQLGeneralError {

    public GQLInsufficientPermissionException(String message) {
        super(message);
    }
}
