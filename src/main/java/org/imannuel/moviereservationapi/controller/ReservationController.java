package org.imannuel.moviereservationapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.constant.Constant;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.reservation.ReservationRequest;
import org.imannuel.moviereservationapi.dto.response.payment.PaymentResponse;
import org.imannuel.moviereservationapi.dto.response.reservation.ReservationPageResponse;
import org.imannuel.moviereservationapi.dto.response.reservation.ReservationResponse;
import org.imannuel.moviereservationapi.dto.response.template.ApiTemplateResponse;
import org.imannuel.moviereservationapi.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.RESERVATIONS_API)
@RequiredArgsConstructor
@Tag(name = "Reservation Management", description = "API for managing movie reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(
            summary = "Create a new reservation",
            description = "Create a new reservation for a movie. Return midtrans redirect payment url.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created reservation", content = @Content(schema = @Schema(implementation = PaymentDataResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @PostMapping
    public ResponseEntity<?> createReservation(
            @RequestBody ReservationRequest reservationRequest
    ) {
        PaymentResponse paymentResponse = reservationService.createReservation(reservationRequest);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Successfully created reservation", paymentResponse);
    }

    @Operation(
            summary = "Cancel a reservation",
            description = "Cancel an existing reservation. Accessible by the user who made the reservation or ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully canceled reservation", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden action", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Reservation not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @permissionEvaluationServiceImpl.hasAccessToReservation(#id, authentication.principal.id))")
    public ResponseEntity<?> cancelReservation(
            @PathVariable("id") String id
    ) {
        reservationService.cancelReservation(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully canceled reservation", null);
    }

    @Operation(
            summary = "Get all user reservations",
            description = "Retrieve all reservations made by the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved all reservations", content = @Content(schema = @Schema(implementation = ListReservationDataResponse.class))),
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllUserReservation(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        ReservationPageResponse reservationPageResponse = reservationService.getAllReservationByUserId(page, size);
        return ApiMapper.paginationMapper(HttpStatus.OK, "Successfully retrieved all user reservations", reservationPageResponse.getReservations(),
                reservationPageResponse.getTotalElements(), reservationPageResponse.getPageSize(), reservationPageResponse.getCurrentPage());
    }

    @Operation(
            summary = "Get reservation by ID",
            description = "Retrieve details of a specific reservation by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved reservation details", content = @Content(schema = @Schema(implementation = ReservationDataResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Reservation not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(
            @PathVariable("id") String id
    ) {
        ReservationResponse reservation = reservationService.getReservationById(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully retrieved reservation", reservation);
    }

    private static class PaymentDataResponse extends ApiTemplateResponse<PaymentResponse> {
    }

    private static class ListReservationDataResponse extends ApiTemplateResponse<ReservationPageResponse> {
    }

    private static class ReservationDataResponse extends ApiTemplateResponse<ReservationResponse> {
    }
}
