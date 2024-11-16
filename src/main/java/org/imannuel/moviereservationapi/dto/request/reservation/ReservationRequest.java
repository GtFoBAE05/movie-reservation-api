package org.imannuel.moviereservationapi.dto.request.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequest {
    @NotBlank(message = "showtimeId is required")
    private String showtimeId;

    @NotEmpty(message = "seatId are required")
    @Size(min = 1, message = "At least one seat id is required")
    private List<String> seatId;
}
