package me.clevecord.scrum.helpers.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import me.clevecord.scrum.errors.UnauthorizedException;
import me.clevecord.scrum.errors.graphql.GQLAuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = 4877950071517252697L;

    @Value("${jwt.validity}")
    private long jwtTokenValidity;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String getJwtTokenFromRequest(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            return requestTokenHeader.substring(7);
        }
        throw new UnauthorizedException("JWT Token not supplied.");
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return generateTokenInternal(claims, userDetails.getUsername());
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private Claims getAllClaimsFromToken(String token) {
        final JwtParser parser = Jwts.parserBuilder()
            .setSigningKey(getEncodedSecret())
            .build();
        return parser.parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String generateTokenInternal(Map<String, Object> claims, String subject) {
        final Key key = Keys.hmacShaKeyFor(getEncodedSecret());
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1000))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    private byte[] getEncodedSecret() {
        return Base64.getEncoder()
            .encode(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
