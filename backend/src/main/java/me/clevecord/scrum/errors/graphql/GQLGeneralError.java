package me.clevecord.scrum.errors.graphql;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

public class GQLGeneralError extends RuntimeException implements GraphQLError {
    
    private static final long serialVersionUID = -8427339197858515084L;

    public GQLGeneralError(final String message) {
        super(message);
    }

    public GQLGeneralError(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorClassification getErrorType() {
        return ErrorType.ExecutionAborted;
    }
}
