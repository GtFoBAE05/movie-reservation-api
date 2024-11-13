package org.imannuel.moviereservationapi.dto.response.genre;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenreResponse {
    private Long id;

    private String name;
}
