package me.clevecord.scrum.domain.board.exceptions;

import me.clevecord.scrum.errors.graphql.GQLGeneralError;

public class GQLBoardNotFoundException extends GQLGeneralError {

    public GQLBoardNotFoundException(String message) {
        super(message);
    }
}
