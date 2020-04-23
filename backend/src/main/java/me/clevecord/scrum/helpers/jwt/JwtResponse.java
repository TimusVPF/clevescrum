package me.clevecord.scrum.helpers.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8659737302029325963L;
    private final String jwtToken;
}
