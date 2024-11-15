package org.imannuel.moviereservationapi.dto.response.showtime;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowtimeListResponse {
    private List<ShowtimeResponse> showtimes;
}
