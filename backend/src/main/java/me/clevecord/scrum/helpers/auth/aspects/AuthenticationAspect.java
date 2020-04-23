package me.clevecord.scrum.helpers.auth.aspects;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.clevecord.scrum.errors.UnauthorizedException;
import me.clevecord.scrum.errors.graphql.GQLAuthException;
import me.clevecord.scrum.helpers.jwt.JwtTokenUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationAspect {

    private final HttpServletRequest request;
    private final JwtTokenUtil jwtTokenUtil;

    @Around("@annotation(me.clevecord.scrum.helpers.auth.annotations.RequiresAuth) && execution(public * *(..))")
    public Object requiresAuth(final ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            final String jwtToken = jwtTokenUtil.getJwtTokenFromRequest(request);
            jwtTokenUtil.getUsernameFromToken(jwtToken);
            return joinPoint.proceed();
        } catch (UnauthorizedException | IllegalArgumentException | ExpiredJwtException e) {
            throw new GQLAuthException("Invalid JWT Token or it has expired");
        }
    }
}
