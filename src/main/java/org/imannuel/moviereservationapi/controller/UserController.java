package org.imannuel.moviereservationapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.constant.Constant;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.user.UpdateUserRequest;
import org.imannuel.moviereservationapi.dto.response.template.ApiTemplateResponse;
import org.imannuel.moviereservationapi.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.USER_API)
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing user accounts and roles")
public class UserController {
    private final UserAccountService userAccountService;

    @Operation(
            summary = "Promote user to admin role",
            description = "Promote an existing user to the admin role. Only accessible by users with ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully promoted to admin", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden: User does not have permission", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @PostMapping("/{id}/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity promoteToAdmin(
            @PathVariable("id") String id
    ) {
        userAccountService.updateRoleToAdmin(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully promoted user to admin", null);
    }

    @Operation(
            summary = "Update user account details",
            description = "Update the user account information, such as email, username, or password.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User details successfully updated", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or parameters", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @PutMapping
    public ResponseEntity updateUserAccount(
            @RequestBody UpdateUserRequest updateUserRequest
    ) {
        userAccountService.updateUserAccount(updateUserRequest);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully updated user account", null);
    }
}
