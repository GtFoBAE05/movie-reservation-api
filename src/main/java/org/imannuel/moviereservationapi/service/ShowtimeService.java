package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.dto.request.showtime.ShowtimeRequest;
import org.imannuel.moviereservationapi.dto.response.Seat.SeatListResponse;
import org.imannuel.moviereservationapi.dto.response.showtime.ShowtimePageResponse;
import org.imannuel.moviereservationapi.dto.response.showtime.ShowtimeResponse;
import org.imannuel.moviereservationapi.entity.Showtime;

public interface ShowtimeService {
    void createShowtime(ShowtimeRequest showtimeRequest);

    void updateShowtime(String id, ShowtimeRequest showtimeRequest);

    Showtime findShowtimeById(String id);

    ShowtimeResponse getShowtimeById(String id);

    ShowtimePageResponse getAllShowtime(Integer page, Integer size);

    ShowtimePageResponse getAllHistoryShowtime(Integer page, Integer size);

    ShowtimePageResponse getShowtimeBy(String date, String movieId, Integer page, Integer size);

    boolean checkIsShowtimeUpdateable(String id);

    SeatListResponse getAvailableSeat(String showtimeId);

    boolean checkIsShowtimeIsReserveable(String showtimeId);
}
