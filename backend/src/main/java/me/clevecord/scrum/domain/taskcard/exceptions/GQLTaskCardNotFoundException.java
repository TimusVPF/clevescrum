package me.clevecord.scrum.domain.taskcard.exceptions;

import me.clevecord.scrum.errors.graphql.GQLGeneralError;

public class GQLTaskCardNotFoundException extends GQLGeneralError {

    public GQLTaskCardNotFoundException(String message) {
        super(message);
    }
}
