package org.imannuel.moviereservationapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.constant.Constant;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.auth.LoginRequest;
import org.imannuel.moviereservationapi.dto.request.auth.RegisterRequest;
import org.imannuel.moviereservationapi.dto.response.auth.LoginResponse;
import org.imannuel.moviereservationapi.dto.response.template.ApiTemplateResponse;
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
@Tag(name = "Authentication", description = "APIs for user login and regsiter")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "User login",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success Register user", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
            })
    @PostMapping("/register")
    public ResponseEntity<ApiTemplateResponse> registerUser(
            @RequestBody RegisterRequest registerRequest
    ) {
        authService.register(registerRequest);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success Register user", null);
    }

    ;

    @Operation(summary = "User login",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success login user", content = @Content(schema = @Schema(implementation = ApiLoginTemplateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid login credentials", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            })
    @PostMapping("/login")
    public ResponseEntity<ApiTemplateResponse> loginUser(
            @RequestBody LoginRequest loginRequest
    ) {
        LoginResponse login = authService.login(loginRequest);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success login user", login);
    }

    private static class ApiLoginTemplateResponse extends ApiTemplateResponse<LoginResponse> {
    }
}
