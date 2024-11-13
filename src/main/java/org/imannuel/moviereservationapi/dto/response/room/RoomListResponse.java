package org.imannuel.moviereservationapi.dto.response.room;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomListResponse {
    private List<RoomResponse> rooms;
}
