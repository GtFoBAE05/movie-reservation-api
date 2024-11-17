package org.imannuel.moviereservationapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.constant.Constant;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.seat.SeatRequest;
import org.imannuel.moviereservationapi.dto.response.Seat.SeatListResponse;
import org.imannuel.moviereservationapi.dto.response.template.ApiTemplateResponse;
import org.imannuel.moviereservationapi.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.SEATS_API)
@RequiredArgsConstructor
@Tag(name = "Seat Management", description = "API for managing movie seats")
public class SeatController {
    private final SeatService seatService;

    @Operation(
            summary = "Create a new seat",
            description = "Create a new seat within a specific room.O nly accessible by users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created seat", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSeat(
            @RequestBody SeatRequest seatRequest
    ) {
        seatService.createSeat(seatRequest);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Successfully created seat", null);
    }

    @Operation(
            summary = "Get seats by room ID",
            description = "Retrieve all seats within a specific room by room ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched seats for the room", content = @Content(schema = @Schema(implementation = ListSeatDataResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Room not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @GetMapping("/room/{id}")
    public ResponseEntity<?> getSeatByRoomId(
            @PathVariable(name = "id") Long id
    ) {
        SeatListResponse seats = seatService.findSeatByRoomId(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully fetched seats within room", seats);
    }

    @Operation(
            summary = "Update seat details",
            description = "Update details of an existing seat by its ID. Only accessible by users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated seat", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Seat not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSeat(
            @PathVariable(name = "id") String id,
            @RequestBody SeatRequest seatRequest
    ) {
        seatService.updateSeat(id, seatRequest);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully updated seat", null);
    }

    @Operation(
            summary = "Delete seat",
            description = "Delete a specific seat by its ID. Only accessible by users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully deleted seat", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Seat not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSeat(
            @PathVariable(name = "id") String id
    ) {
        seatService.deleteSeat(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully deleted seat", null);
    }

    private static class ListSeatDataResponse extends ApiTemplateResponse<SeatListResponse> {
    }
}
