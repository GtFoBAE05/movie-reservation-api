package org.imannuel.moviereservationapi.dto.request.room;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomRequest {
    @NotBlank(message = "name is required")
    private String name;
}
