package org.imannuel.moviereservationapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.request.auth.LoginRequest;
import org.imannuel.moviereservationapi.dto.request.auth.RegisterRequest;
import org.imannuel.moviereservationapi.dto.response.auth.LoginResponse;
import org.imannuel.moviereservationapi.entity.UserAccount;
import org.imannuel.moviereservationapi.service.AuthService;
import org.imannuel.moviereservationapi.service.JwtService;
import org.imannuel.moviereservationapi.service.UserAccountService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserAccountService userAccountService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @Override
    public void register(RegisterRequest registerRequest) {
        UserAccount userAccount = UserAccount.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .build();
        userAccountService.createUserAccount(userAccount);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        UserAccount userAccount = (UserAccount) authenticate.getPrincipal();

        return LoginResponse.builder()
                .accessToken(jwtService.generateToken(userAccount))
                .build();
    }

}
