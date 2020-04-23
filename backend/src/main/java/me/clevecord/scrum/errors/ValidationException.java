package me.clevecord.scrum.errors;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String message;

    public ValidationException(String message) {
        super(message);
    }
}
