package org.imannuel.moviereservationapi.dto.request.showtime;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowtimeRequest {
    private String movieId;

    private Long roomId;

    private String startDateTime;

    private Long price;
}
