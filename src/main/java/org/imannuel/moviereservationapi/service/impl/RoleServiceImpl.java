package org.imannuel.moviereservationapi.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.constant.RoleEnum;
import org.imannuel.moviereservationapi.entity.Role;
import org.imannuel.moviereservationapi.repository.RoleRepository;
import org.imannuel.moviereservationapi.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

//    @PostConstruct
//    @Transactional(rollbackFor = Exception.class)
//    public void initRole() {
//        for (RoleEnum value : RoleEnum.values()) {
//            Boolean isRoleExist = checkIsRoleExist(value.name());
//            if (!isRoleExist) {
//                createRole(value.name());
//            }
//        }
//    }

    @Override
    public Boolean checkIsRoleExist(String name) {
        return roleRepository.existsRoleByName(name);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(String name) {
        roleRepository.insertRole(name);
    }

    public Role findRoleByName(String name) {
        return roleRepository.findRoleByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
    }

    @Override
    public Role getRoleByName(String name) {
        return findRoleByName(name);
    }
}
