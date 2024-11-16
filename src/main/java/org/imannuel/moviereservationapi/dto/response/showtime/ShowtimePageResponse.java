package org.imannuel.moviereservationapi.dto.response.showtime;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowtimePageResponse {
    private List<ShowtimeResponse> showtimes;

    private long totalElements;

    private int totalPages;

    private int currentPage;

    private int pageSize;
}
