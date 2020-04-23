package me.clevecord.scrum.errors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ErrorBody {
    private String errorMessage;
    private String errorStatus;
    private int errorCode;
}
