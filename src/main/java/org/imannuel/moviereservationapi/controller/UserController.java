package org.imannuel.moviereservationapi.controller;

import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.constant.Constant;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.user.UpdateUserRequest;
import org.imannuel.moviereservationapi.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.USER_API)
@RequiredArgsConstructor
public class UserController {
    private final UserAccountService userAccountService;

    @PostMapping("/{id}/promote")
    public ResponseEntity promoteToAdmin(
            @PathVariable("id") String id
     ) {
        userAccountService.updateRoleToAdmin(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success promote user to admin", null);
    }

    @PutMapping()
    public ResponseEntity updateUserAccount(
            @RequestBody UpdateUserRequest updateUserRequest
    ) {
        userAccountService.updateUserAccount(updateUserRequest);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success update user", null);
    }
}
