package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.dto.request.auth.PromoteToAdminRequest;
import org.imannuel.moviereservationapi.dto.request.user.UpdateUserRequest;
import org.imannuel.moviereservationapi.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAccountService extends UserDetailsService {
    void createUserAccount(UserAccount userAccount);

    UserAccount getUserAccountById(String id);

    void updateUserAccount(UpdateUserRequest updateUserRequest);

    void updateRoleToAdmin(PromoteToAdminRequest promoteToAdminRequest);
}
