package org.imannuel.moviereservationapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.showtime.ShowtimeRequest;
import org.imannuel.moviereservationapi.dto.response.Seat.SeatListResponse;
import org.imannuel.moviereservationapi.dto.response.showtime.ShowtimePageResponse;
import org.imannuel.moviereservationapi.dto.response.showtime.ShowtimeResponse;
import org.imannuel.moviereservationapi.dto.response.template.ApiTemplateResponse;
import org.imannuel.moviereservationapi.service.ShowtimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/showtimes")
@RequiredArgsConstructor
@Tag(name = "Showtime Management", description = "APIs for managing movie showtimes")
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @Operation(
            summary = "Create a new showtime",
            description = "Create a new showtime for a movie in a specific room and at a scheduled time. Only accessible by users with ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created showtime", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createShowtime(
            @RequestBody ShowtimeRequest showtimeRequest
    ) {
        showtimeService.createShowtime(showtimeRequest);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Successfully created showtime", null);
    }

    @Operation(
            summary = "Update an existing showtime",
            description = "Update the details of an existing showtime using its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated showtime", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Showtime not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateShowtime(
            @PathVariable(name = "id") String id,
            @RequestBody ShowtimeRequest showtimeRequest
    ) {
        showtimeService.updateShowtime(id, showtimeRequest);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully updated showtime", null);
    }

    @Operation(
            summary = "Get a showtime by ID",
            description = "Retrieve the details of a specific showtime by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved showtime", content = @Content(schema = @Schema(implementation = ShowtimeDataResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Showtime not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity getShowtimeById(
            @PathVariable(name = "id") String id
    ) {
        ShowtimeResponse showtime = showtimeService.getShowtimeById(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully retrieved showtime", showtime);
    }

    @Operation(
            summary = "Get available seats for a specific showtime",
            description = "Retrieve the list of available seats for a given showtime.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved available seats", content = @Content(schema = @Schema(implementation = SeatListDataResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Showtime not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @GetMapping("/{id}/seats")
    public ResponseEntity getAvailableSeatShowtime(
            @PathVariable(name = "id") String id
    ) {
        SeatListResponse availableSeat = showtimeService.getAvailableSeat(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully retrieved available seats", availableSeat);
    }

    @Operation(
            summary = "Get all showtimes",
            description = "Retrieve a list of all showtimes available.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched all showtimes", content = @Content(schema = @Schema(implementation = ListShowtimeDataResponse.class)))
            }
    )
    @GetMapping("/all")
    public ResponseEntity getAllShowtime(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        ShowtimePageResponse showtimePageResponse = showtimeService.getAllShowtime(page, size);
        return ApiMapper.paginationMapper(HttpStatus.OK, "Successfully fetched all showtimes", showtimePageResponse.getShowtimes(),
                showtimePageResponse.getTotalElements(), showtimePageResponse.getPageSize(), showtimePageResponse.getCurrentPage());
    }

    @Operation(
            summary = "Get showtimes filtered by date or movie",
            description = "Retrieve a list of showtimes filtered by date or movie ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched showtimes", content = @Content(schema = @Schema(implementation = ListShowtimeDataResponse.class))),
                    @ApiResponse(responseCode = "404", description = "No showtimes found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @GetMapping
    public ResponseEntity getAvailableShowtimeBy(
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "movie", required = false) String movieId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        ShowtimePageResponse showtimePageResponse = showtimeService.getShowtimeBy(date, movieId, page, size);
        return ApiMapper.paginationMapper(HttpStatus.OK, "Successfully fetched showtimes", showtimePageResponse.getShowtimes(),
                showtimePageResponse.getTotalElements(), showtimePageResponse.getPageSize(), showtimePageResponse.getCurrentPage());
    }

    private static class SeatListDataResponse extends ApiTemplateResponse<SeatListResponse> {
    }

    private static class ListShowtimeDataResponse extends ApiTemplateResponse<ShowtimePageResponse> {
    }

    private static class ShowtimeDataResponse extends ApiTemplateResponse<ShowtimeResponse> {
    }
}
