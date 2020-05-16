package me.clevecord.scrum.domain.category.exceptions;

import me.clevecord.scrum.errors.graphql.GQLGeneralError;

public class GQLBoardCategoryNotFoundException extends GQLGeneralError {

    public GQLBoardCategoryNotFoundException(String message) {
        super(message);
    }
}
