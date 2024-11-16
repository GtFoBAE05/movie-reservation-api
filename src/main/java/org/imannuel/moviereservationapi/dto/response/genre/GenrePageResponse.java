package org.imannuel.moviereservationapi.dto.response.genre;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenrePageResponse {
    private List<GenreResponse> genres;

    private long totalElements;

    private int totalPages;

    private int currentPage;

    private int pageSize;
}
