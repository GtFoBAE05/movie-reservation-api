package org.imannuel.moviereservationapi.dto.mapper;

import org.imannuel.moviereservationapi.dto.response.showtime.ShowtimeResponse;
import org.imannuel.moviereservationapi.entity.Showtime;

import java.util.List;

public class ShowtimeMapper {
    public static ShowtimeResponse showtimeToShowtimeResponse(Showtime showtime) {
        return ShowtimeResponse.builder()
                .id(showtime.getId().toString())
                .movie(MovieMapper.movieToMovieResponse(showtime.getMovie()))
                .room(RoomMapper.roomToRoomWithoutSeatResponse(showtime.getRoom()))
                .startDateTime(showtime.getStartDateTime().toString())
                .price(showtime.getPrice())
                .build();
    }

    public static List<ShowtimeResponse> showtimeListToShowTimeListResponse(List<Showtime> showtimes) {
        return showtimes.stream().map(showtime -> showtimeToShowtimeResponse(showtime)).toList();
    }
}
