package org.imannuel.moviereservationapi.controller;

import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.seat.SeatRequest;
import org.imannuel.moviereservationapi.dto.response.Seat.SeatListResponse;
import org.imannuel.moviereservationapi.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createSeat(
            @RequestBody SeatRequest seatRequest
    ) {
        seatService.createSeat(seatRequest);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Success create seat", null);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity getSeatByRoomId(
            @PathVariable(name = "id") Long id
    ) {
        SeatListResponse seats = seatService.findSeatByRoomId(id);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Success fetch seat within room", seats);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateSeat(
            @PathVariable(name = "id") String id,
            @RequestBody SeatRequest seatRequest
    ) {
        seatService.updateSeat(id, seatRequest);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Success update seat", null);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteSeat(
            @PathVariable(name = "id") String id
    ) {
        seatService.deleteSeat(id);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Success delete seat", null);
    }
}
