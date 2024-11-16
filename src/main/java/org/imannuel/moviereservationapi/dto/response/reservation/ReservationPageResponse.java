package org.imannuel.moviereservationapi.dto.response.reservation;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationPageResponse {
    private List<ReservationResponse> reservations;

    private long totalElements;

    private int totalPages;

    private int currentPage;

    private int pageSize;
}
