package org.imannuel.moviereservationapi.dto.request.movie;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieRequest {
    private String title;

    private String description;

    private Integer durationInMinute;

    private String posterImage;

    private List<Long> genres;

    private String releaseDate;
}
