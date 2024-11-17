package org.imannuel.moviereservationapi.service.impl;

import org.imannuel.moviereservationapi.dto.request.auth.LoginRequest;
import org.imannuel.moviereservationapi.dto.request.auth.RegisterRequest;
import org.imannuel.moviereservationapi.dto.response.auth.LoginResponse;
import org.imannuel.moviereservationapi.entity.UserAccount;
import org.imannuel.moviereservationapi.service.JwtService;
import org.imannuel.moviereservationapi.service.UserAccountService;
import org.imannuel.moviereservationapi.utils.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserAccountService userAccountService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private ValidationUtil validationUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldCallCreateAccountWhenRegister() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("budi")
                .email("budi@")
                .password("password")
                .build();
        Mockito.doNothing().when(validationUtil).validate(registerRequest);
        Mockito.doNothing().when(userAccountService).createUserAccount(Mockito.any(UserAccount.class));

        authService.register(registerRequest);

        Mockito.verify(userAccountService, Mockito.times(1)).createUserAccount(Mockito.any(UserAccount.class));
    }

    @Test
    void shouldGenerateAccessTokenWhenLoginIsSuccessful() {
        LoginRequest loginRequest = new LoginRequest("admin", "password");
        UserAccount userAccount = UserAccount.builder()
                .id(UUID.randomUUID())
                .username("admin")
                .build();
        Authentication mockAuthentication = Mockito.mock(Authentication.class);
        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(userAccount);
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        String jwt = "jwt";
        Mockito.when(jwtService.generateToken(userAccount)).thenReturn(jwt);

        LoginResponse login = authService.login(loginRequest);

        assertEquals(jwt, login.getAccessToken());
    }
}