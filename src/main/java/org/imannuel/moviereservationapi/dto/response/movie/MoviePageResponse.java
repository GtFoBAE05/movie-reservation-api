package org.imannuel.moviereservationapi.dto.response.movie;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoviePageResponse {
    private List<MovieResponse> movies;

    private long totalElements;

    private int totalPages;

    private int currentPage;

    private int pageSize;
}
