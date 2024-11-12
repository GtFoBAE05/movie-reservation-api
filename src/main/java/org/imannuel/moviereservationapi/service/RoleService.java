package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.entity.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    Boolean checkIsRoleExist(String name);

    void createRole(String name);

    Role getRoleByName(String name);
}
