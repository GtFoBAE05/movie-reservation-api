package org.imannuel.moviereservationapi.dto.response.movie;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieListResponse {
    private List<MovieResponse> movies;
}