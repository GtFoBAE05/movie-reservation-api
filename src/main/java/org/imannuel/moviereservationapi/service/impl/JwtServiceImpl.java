package org.imannuel.moviereservationapi.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.moviereservationapi.entity.UserAccount;
import org.imannuel.moviereservationapi.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${movie.reservation.jwt-secret}")
    private String SECRET_KEY;
    @Value("${movie.reservation.jwt-expiration-in-minutes-access-token}")
    private Long EXPIRATION_IN_MINUTES;
    @Value("${movie.reservation.jwt-issuer}")
    private String ISSUER;

    @Override
    public String generateToken(UserAccount userAccount) {
        log.info("Generating JWT Token for userAccount with id " + userAccount.getId());
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            return JWT.create()
                    .withExpiresAt(Instant.now().plus(EXPIRATION_IN_MINUTES, ChronoUnit.MINUTES))
                    .withIssuer(ISSUER)
                    .withSubject(userAccount.getId().toString())
                    .withClaim("role", userAccount.getRole().getName().toString())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            log.error("Error creating token for userAccount with id " + userAccount.getId());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error when creating JWT");
        }
    }

    @Override
    public String extractSubject(String token) {
        DecodedJWT decodedJWT = extractClaimJWT(token);
        if (decodedJWT != null) {
            return decodedJWT.getSubject();
        }
        return null;
    }

    @Override
    public String extractTokenFromRequest(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        return parseToken(bearerToken);
    }

    @Override
    public DecodedJWT extractClaimJWT(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    @Override
    public String parseToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
