package org.imannuel.moviereservationapi.dto.response.showtime;

import lombok.*;
import org.imannuel.moviereservationapi.dto.response.movie.MovieResponse;
import org.imannuel.moviereservationapi.dto.response.room.RoomWithoutSeatResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowtimeResponse {
    private String id;

    private MovieResponse movie;

    private RoomWithoutSeatResponse room;

    private String startDateTime;

    private Long price;
}
