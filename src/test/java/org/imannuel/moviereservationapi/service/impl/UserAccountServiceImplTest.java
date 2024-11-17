package org.imannuel.moviereservationapi.service.impl;

import org.imannuel.moviereservationapi.constant.RoleEnum;
import org.imannuel.moviereservationapi.dto.request.user.UpdateUserRequest;
import org.imannuel.moviereservationapi.entity.Role;
import org.imannuel.moviereservationapi.entity.UserAccount;
import org.imannuel.moviereservationapi.repository.UserAccountRepository;
import org.imannuel.moviereservationapi.service.RoleService;
import org.imannuel.moviereservationapi.utils.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceImplTest {
    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @Mock
    private ValidationUtil validationUtil;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    @Mock
    private Authentication authentication;

    @Test
    void shouldCallInsertUserAccountWhenCreateUserAccount() {
        Role role = Role.builder()
                .id(1)
                .name(RoleEnum.ROLE_ADMIN)
                .build();
        UserAccount userAccount = UserAccount.builder()
                .id(UUID.randomUUID())
                .role(role)
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .email("admin@")
                .build();
        Mockito.doNothing().when(userAccountRepository).insertUserAccount(userAccount);

        userAccountService.createUserAccount(userAccount);

        Mockito.verify(userAccountRepository, Mockito.times(1)).insertUserAccount(userAccount);
    }

    @Test
    void shouldReturnUserAccountWhenGetUserAccountById() {
        Role role = Role.builder()
                .id(1)
                .name(RoleEnum.ROLE_ADMIN)
                .build();
        UserAccount expectedUserAccount = UserAccount.builder()
                .id(UUID.randomUUID())
                .role(role)
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .email("admin@")
                .build();
        Mockito.when(userAccountRepository.findUserAccountById(expectedUserAccount.getId())).thenReturn(Optional.of(expectedUserAccount));

        UserAccount userAccount = userAccountService.getUserAccountById(expectedUserAccount.getId().toString());

        assertEquals(expectedUserAccount.getId(), userAccount.getId());
        assertEquals(expectedUserAccount.getRole(), userAccount.getRole());
        assertEquals(expectedUserAccount.getUsername(), userAccount.getUsername());
        assertEquals(expectedUserAccount.getEmail(), userAccount.getEmail());
        assertEquals(expectedUserAccount.getPassword(), userAccount.getPassword());
        Mockito.verify(userAccountRepository, Mockito.times(1)).findUserAccountById(expectedUserAccount.getId());
    }

    @Test
    void shouldThrowErrorWhenFindUserById() {
        UUID id = UUID.randomUUID();
        Mockito.when(userAccountRepository.findUserAccountById(id)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userAccountService.findUserAccountById(id.toString()));

        assertEquals("User not found", exception.getReason());
        Mockito.verify(userAccountRepository, Mockito.times(1))
                .findUserAccountById(id);
    }


    @Test
    void shouldCallUpdateUserAccountWhenUpdateUserAccount() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Role role = Role.builder()
                .id(1)
                .name(RoleEnum.ROLE_ADMIN)
                .build();
        UserAccount userAccount = UserAccount.builder()
                .id(UUID.randomUUID())
                .role(role)
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .email("admin@")
                .build();
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("newUsername", "newEmail");
        Mockito.doNothing().when(validationUtil).validate(updateUserRequest);
        Mockito.when(authentication.getPrincipal()).thenReturn(userAccount);
        Mockito.doNothing().when(userAccountRepository).updateUserAccount(userAccount.getId(), updateUserRequest.getUsername(), updateUserRequest.getEmail());

        userAccountService.updateUserAccount(updateUserRequest);

        Mockito.verify(validationUtil, Mockito.times(1)).validate(updateUserRequest);
        Mockito.verify(userAccountRepository, Mockito.times(1)).updateUserAccount(userAccount.getId(), updateUserRequest.getUsername(), updateUserRequest.getEmail());
    }

    @Test
    void shouldCallUpdateRoleToAdminWhenUpdateRoleToAdmin() {
        Role roleAdmin = Role.builder()
                .id(1)
                .name(RoleEnum.ROLE_ADMIN)
                .build();
        Role roleUser = Role.builder()
                .id(1)
                .name(RoleEnum.ROLE_USER)
                .build();
        UserAccount userAccount = UserAccount.builder()
                .id(UUID.randomUUID())
                .role(roleUser)
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .email("admin@")
                .build();
        Mockito.when(userAccountRepository.findUserAccountById(userAccount.getId())).thenReturn(Optional.of(userAccount));
        Mockito.when(roleService.getRoleByName(RoleEnum.ROLE_ADMIN.name())).thenReturn(roleAdmin);

        userAccountService.updateRoleToAdmin(userAccount.getId().toString());

        Mockito.verify(userAccountRepository, Mockito.times(1)).updateRoleToAdmin(userAccount.getId().toString(), roleAdmin.getId());
        Mockito.verify(roleService, Mockito.times(1)).getRoleByName(roleAdmin.getName().name());
    }

    @Test
    void shouldThrowErrorWhenUpdateRoleToAdmin() {
        Role role = Role.builder()
                .id(1)
                .name(RoleEnum.ROLE_ADMIN)
                .build();
        UserAccount userAccount = UserAccount.builder()
                .id(UUID.randomUUID())
                .role(role)
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .email("admin@")
                .build();
        Mockito.when(userAccountRepository.findUserAccountById(userAccount.getId())).thenReturn(Optional.of(userAccount));
        Mockito.when(roleService.getRoleByName(RoleEnum.ROLE_ADMIN.name())).thenReturn(role);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userAccountService.updateRoleToAdmin(userAccount.getId().toString()));

        assertEquals("User role already admin", exception.getReason());
    }

    @Test
    void shouldReturnUserDetailsWhenLoadByUsername() {
        Role role = Role.builder()
                .id(1)
                .name(RoleEnum.ROLE_ADMIN)
                .build();
        UserAccount userAccount = UserAccount.builder()
                .id(UUID.randomUUID())
                .role(role)
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .email("admin@")
                .build();

        Mockito.when(userAccountRepository.findUserAccountByUsername(userAccount.getUsername()))
                .thenReturn(Optional.of(userAccount));

        UserDetails userDetails = userAccountService.loadUserByUsername(userAccount.getUsername());

        assertEquals(userAccount.getUsername(), userDetails.getUsername());
        assertEquals(userAccount.getPassword(), userDetails.getPassword());
        assertEquals(userAccount.getAuthorities(), userDetails.getAuthorities());
        Mockito.verify(userAccountRepository, Mockito.times(1)).findUserAccountByUsername(userAccount.getUsername());
    }

    @Test
    void shouldThrowErrorsWhenLoadByUsername() {
        String username = "budi";
        Mockito.when(userAccountRepository.findUserAccountByUsername(username))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userAccountService.loadUserByUsername(username));

        assertEquals("Invalid Credential", exception.getMessage());
        Mockito.verify(userAccountRepository, Mockito.times(1)).findUserAccountByUsername(username);
    }

}