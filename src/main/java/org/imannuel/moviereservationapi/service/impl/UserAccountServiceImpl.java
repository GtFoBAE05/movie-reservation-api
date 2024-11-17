package org.imannuel.moviereservationapi.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.constant.RoleEnum;
import org.imannuel.moviereservationapi.dto.request.user.UpdateUserRequest;
import org.imannuel.moviereservationapi.entity.Role;
import org.imannuel.moviereservationapi.entity.UserAccount;
import org.imannuel.moviereservationapi.repository.UserAccountRepository;
import org.imannuel.moviereservationapi.service.RoleService;
import org.imannuel.moviereservationapi.service.UserAccountService;
import org.imannuel.moviereservationapi.utils.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final ValidationUtil validationUtil;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    void init() {
        if (!existsByUsername("admin")) {
            Role role = roleService.getRoleByName(RoleEnum.ROLE_ADMIN.name());
            UserAccount userAccount = UserAccount.builder()
                    .id(UUID.randomUUID())
                    .username("admin")
                    .email("admin@")
                    .password(passwordEncoder.encode("secretpassword"))
                    .role(role)
                    .build();
            userAccountRepository.insertUserAccount(userAccount);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUserAccount(UserAccount userAccount) {
        Role role = roleService.getRoleByName(RoleEnum.ROLE_USER.name());
        UUID uuid = UUID.randomUUID();
        userAccount.setId(uuid);
        userAccount.setRole(role);
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        userAccountRepository.insertUserAccount(userAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAccount getUserAccountById(String id) {
        return findUserAccountById(id);
    }

    public UserAccount findUserAccountById(String id) {
        return userAccountRepository.findUserAccountById(UUID.fromString(id)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAccount(UpdateUserRequest updateUserRequest) {
        validationUtil.validate(updateUserRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        userAccountRepository.updateUserAccount(userAccount.getId(), updateUserRequest.getUsername(), updateUserRequest.getEmail());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleToAdmin(String id) {
        UserAccount userAccount = findUserAccountById(id);
        Role role = roleService.getRoleByName(RoleEnum.ROLE_ADMIN.name());
        if (userAccount.getRole().getName().equals(role.getName())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User role already admin");
        }
        userAccountRepository.updateRoleToAdmin(id, role.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findUserAccountByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Invalid Credential")
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userAccountRepository.existsUserAccountByUsername(username);
    }
}
