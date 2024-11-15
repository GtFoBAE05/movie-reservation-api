package org.imannuel.moviereservationapi.dto.response.room;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomWithoutSeatResponse {
    private Long id;

    private String name;
}
