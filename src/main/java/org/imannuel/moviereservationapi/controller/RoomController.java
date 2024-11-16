package org.imannuel.moviereservationapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.room.RoomRequest;
import org.imannuel.moviereservationapi.dto.response.room.RoomListResponse;
import org.imannuel.moviereservationapi.dto.response.room.RoomResponse;
import org.imannuel.moviereservationapi.dto.response.template.ApiTemplateResponse;
import org.imannuel.moviereservationapi.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/rooms")
@RequiredArgsConstructor
@Tag(name = "Room Management", description = "API for managing movie rooms.")
public class RoomController {
    private final RoomService roomService;

    @Operation(
            summary = "Create a new room",
            description = "Create a new movie room with necessary details. Only accessible by users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created room", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createRoom(
            @RequestBody RoomRequest roomRequest
    ) {
        roomService.createRoom(roomRequest);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Successfully created room", null);
    }

    @Operation(
            summary = "Get all rooms",
            description = "Retrieve a list of all available rooms. ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched all rooms", content = @Content(schema = @Schema(implementation = ListRoomDataResponse.class))),
            }
    )
    @GetMapping
    public ResponseEntity getAllRoom() {
        RoomListResponse rooms = roomService.getAllRoom();
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully fetched all rooms", rooms);
    }

    @Operation(
            summary = "Get room by ID",
            description = "Retrieve the details of a specific room by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved room details", content = @Content(schema = @Schema(implementation = RoomDataResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Room not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity getRoomById(
            @PathVariable("id") Long id
    ) {
        RoomResponse room = roomService.getRoomById(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully retrieved room", room);
    }

    @Operation(
            summary = "Update room details",
            description = "Update the details of an existing room by its ID. Only accessible by users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated room", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Room not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateRoom(
            @PathVariable("id") Long id,
            @RequestBody RoomRequest roomRequest
    ) {
        roomService.updateRoom(id, roomRequest);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully updated room", null);
    }

    @Operation(
            summary = "Delete room",
            description = "Delete an existing room by its ID. Only accessible by users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully deleted room", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Room not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteRoom(
            @PathVariable("id") Long id
    ) {
        roomService.deleteRoom(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully deleted room", null);
    }

    private static class ListRoomDataResponse extends ApiTemplateResponse<List<RoomResponse>> {
    }

    private static class RoomDataResponse extends ApiTemplateResponse<RoomResponse> {
    }
}
