package me.clevecord.scrum.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class BaseErrorHandler {

    @ResponseBody
    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<?> handleException(ValidationException exc) {
        return ResponseEntity.badRequest()
            .body(formErrorResult(exc, HttpStatus.BAD_REQUEST));
    }

    private ErrorBody formErrorResult(Exception exc, HttpStatus status) {
        return ErrorBody.builder()
            .errorCode(status.value())
            .errorStatus(status.getReasonPhrase())
            .errorMessage(exc.getMessage())
            .build();
    }
}
