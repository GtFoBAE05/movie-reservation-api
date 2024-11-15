package org.imannuel.moviereservationapi.dto.response.movie;

import lombok.*;
import org.imannuel.moviereservationapi.dto.response.genre.GenreResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieResponse {
    private String id;

    private String title;

    private String description;

    private Integer durationInMinute;

    private List<String> posterImage;

    private List<GenreResponse> genres;

    private String releaseDate;
}
