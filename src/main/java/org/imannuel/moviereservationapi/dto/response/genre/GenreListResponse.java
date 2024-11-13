package org.imannuel.moviereservationapi.dto.response.genre;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenreListResponse {
    private List<GenreResponse> genres;
}
