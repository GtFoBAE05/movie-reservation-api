package org.imannuel.moviereservationapi.service.impl;

import org.imannuel.moviereservationapi.constant.RoleEnum;
import org.imannuel.moviereservationapi.entity.Role;
import org.imannuel.moviereservationapi.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void shouldReturnBooleanWhenCheckIsRoleExists() {
        String name = "ROLE_ADMIN";
        Boolean expectedResult = true;
        Mockito.when(roleRepository.existsRoleByName(name)).thenReturn(true);

        Boolean isRoleExist = roleService.checkIsRoleExist(name);

        assertEquals(expectedResult, isRoleExist);
        Mockito.verify(roleRepository, Mockito.times(1)).existsRoleByName(name);
    }

    @Test
    void shouldCallInsertRoleWhenCreateRole() {
        String name = "ROLE_ADMIN";
        Mockito.doNothing().when(roleRepository).insertRole(name);

        roleService.createRole(name);

        Mockito.verify(roleRepository, Mockito.times(1)).insertRole(name);
    }

    @Test
    void shouldReturnRoleWhenFindRoleByName() {
        String name = "ROLE_ADMIN";
        Role expectedRole = new Role(1, RoleEnum.ROLE_ADMIN);
        Mockito.when(roleRepository.findRoleByName(name)).thenReturn(Optional.of(expectedRole));

        Role role = roleService.findRoleByName(name);

        assertEquals(expectedRole.getId(), role.getId());
        assertEquals(expectedRole.getName(), role.getName());
        Mockito.verify(roleRepository, Mockito.times(1)).findRoleByName(name);
    }

    @Test
    void shouldThrowErrorWhenFindRoleByName() {
        String name = "ROLE_1";
        Mockito.when(roleRepository.findRoleByName(name)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> roleService.findRoleByName(name));

        assertEquals("Role not found", exception.getReason());
        Mockito.verify(roleRepository, Mockito.times(1)).findRoleByName(name);
    }

    @Test
    void shouldReturnRoleWhenGetRoleByName() {
        String name = "ROLE_ADMIN";
        Role expectedRole = new Role(1, RoleEnum.ROLE_ADMIN);
        Mockito.when(roleRepository.findRoleByName(name)).thenReturn(Optional.of(expectedRole));

        Role role = roleService.getRoleByName(name);

        assertEquals(expectedRole.getId(), role.getId());
        assertEquals(expectedRole.getName(), role.getName());
        Mockito.verify(roleRepository, Mockito.times(1)).findRoleByName(name);
    }
}