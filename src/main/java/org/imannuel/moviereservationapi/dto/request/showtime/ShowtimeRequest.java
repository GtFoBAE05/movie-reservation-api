package org.imannuel.moviereservationapi.dto.request.showtime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowtimeRequest {
    @NotBlank(message = "movieId is required")
    private String movieId;

    @NotNull(message = "roomId is required")
    private Long roomId;

    @NotBlank(message = "startDateTime is required")
    private String startDateTime;

    @NotNull(message = "price is required")
    @Positive(message = "price must be greater than zero")
    private Long price;
}
