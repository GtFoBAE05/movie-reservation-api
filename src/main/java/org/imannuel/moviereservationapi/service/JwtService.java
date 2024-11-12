package org.imannuel.moviereservationapi.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.imannuel.moviereservationapi.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount userAccount);

    String extractSubject(String token);

    String extractTokenFromRequest(HttpServletRequest httpServletRequest);

    DecodedJWT extractClaimJWT(String token);

    String parseToken(String bearerToken);
}

