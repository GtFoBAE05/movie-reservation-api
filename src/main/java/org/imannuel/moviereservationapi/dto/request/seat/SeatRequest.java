package org.imannuel.moviereservationapi.dto.request.seat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatRequest {
    @NotBlank(message = "seatCode is required")
    private String seatCode;

    @NotNull(message = "roomId is required")
    private Long roomId;
}
