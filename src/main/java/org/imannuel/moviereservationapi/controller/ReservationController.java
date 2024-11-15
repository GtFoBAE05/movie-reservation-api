package org.imannuel.moviereservationapi.controller;

import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.reservation.ReservationRequest;
import org.imannuel.moviereservationapi.dto.response.reservation.ReservationListResponse;
import org.imannuel.moviereservationapi.dto.response.reservation.ReservationResponse;
import org.imannuel.moviereservationapi.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity createReservation(
            @RequestBody ReservationRequest reservationRequest
    ) {
        reservationService.createReservation(reservationRequest);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Success create reservation", null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity cancelReservation(
            @PathVariable("id") String id
    ) {
        reservationService.cancelReservation(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success cancel reservation", null);
    }

    @GetMapping
    public ResponseEntity getAllUserReservation() {
        ReservationListResponse allReservationByUserId = reservationService.getAllReservationByUserId();
        return ApiMapper.basicMapper(HttpStatus.OK, "Success fetch all user reservation", allReservationByUserId);
    }

    @GetMapping("/{id}")
    public ResponseEntity getReservationById(
            @PathVariable("id") String id
    ) {
        ReservationResponse reservation = reservationService.getReservationById(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success get reservation", reservation);
    }
}
