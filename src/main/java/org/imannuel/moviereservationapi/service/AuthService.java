package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.dto.request.auth.LoginRequest;
import org.imannuel.moviereservationapi.dto.request.auth.RegisterRequest;
import org.imannuel.moviereservationapi.dto.response.auth.LoginResponse;

public interface AuthService {
    void register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);
}
