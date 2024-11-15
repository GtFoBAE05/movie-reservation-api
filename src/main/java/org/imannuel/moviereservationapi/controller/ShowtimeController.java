package org.imannuel.moviereservationapi.controller;

import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.showtime.ShowtimeRequest;
import org.imannuel.moviereservationapi.dto.response.Seat.SeatListResponse;
import org.imannuel.moviereservationapi.dto.response.showtime.ShowtimeListResponse;
import org.imannuel.moviereservationapi.dto.response.showtime.ShowtimeResponse;
import org.imannuel.moviereservationapi.service.ShowtimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/showtimes")
@RequiredArgsConstructor
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @PostMapping
    public ResponseEntity createShowtime(
            @RequestBody ShowtimeRequest showtimeRequest
    ) {
        showtimeService.createShowtime(showtimeRequest);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Success create showtime", null);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateShowtime(
            @PathVariable(name = "id") String id,
            @RequestBody ShowtimeRequest showtimeRequest
    ) {
        showtimeService.updateShowtime(id, showtimeRequest);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success update showtime", null);
    }

    @GetMapping("/{id}")
    public ResponseEntity getShowtimeById(
            @PathVariable(name = "id") String id
    ) {
        ShowtimeResponse showtime = showtimeService.getShowtimeById(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success get showtime", showtime);
    }

    @GetMapping("/{id}/seats")
    public ResponseEntity getAvailableSeatShowtime(
            @PathVariable(name = "id") String id
    ) {
        SeatListResponse availableSeat = showtimeService.getAvailableSeat(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success get available seats", availableSeat);
    }

    @GetMapping("/all")
    public ResponseEntity getAllShowtime(
    ) {
        ShowtimeListResponse allShowtime = showtimeService.getAllShowtime();
        return ApiMapper.basicMapper(HttpStatus.OK, "Success fetch all showtime", allShowtime);
    }

    @GetMapping
    public ResponseEntity getAvailableShowtimeBy(
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "movie", required = false) String movieId
    ) {
        ShowtimeListResponse showtimes = showtimeService.getShowtimeBy(date, movieId);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success fetch showtime", showtimes);
    }
}
