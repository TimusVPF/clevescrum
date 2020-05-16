package me.clevecord.scrum.domain.task.exceptions;

import me.clevecord.scrum.errors.graphql.GQLGeneralError;

public class GQLTaskNotFoundException extends GQLGeneralError {

    public GQLTaskNotFoundException(String message) {
        super(message);
    }
}
