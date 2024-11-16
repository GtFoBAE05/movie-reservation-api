package org.imannuel.moviereservationapi.dto.request.genre;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenreRequest {
    @NotBlank(message = "name is required")
    private String name;
}
