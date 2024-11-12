package org.imannuel.moviereservationapi.controller;

import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.constant.Constant;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.auth.LoginRequest;
import org.imannuel.moviereservationapi.dto.request.auth.RegisterRequest;
import org.imannuel.moviereservationapi.dto.response.auth.LoginResponse;
import org.imannuel.moviereservationapi.dto.response.template.ApiResponse;
import org.imannuel.moviereservationapi.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = Constant.AUTH_API)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(
            @RequestBody RegisterRequest registerRequest
    ) {
        authService.register(registerRequest);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success Register user", null);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(
            @RequestBody LoginRequest loginRequest
    ) {
        LoginResponse login = authService.login(loginRequest);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success login user", login);
    }
}
