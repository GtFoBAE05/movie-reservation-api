package org.imannuel.moviereservationapi.dto.response.room;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomPageResponse {
    private List<RoomResponse> rooms;

    private long totalElements;

    private int totalPages;

    private int currentPage;

    private int pageSize;
}
