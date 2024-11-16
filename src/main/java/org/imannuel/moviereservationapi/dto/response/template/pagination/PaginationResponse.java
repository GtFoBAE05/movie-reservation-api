package org.imannuel.moviereservationapi.dto.response.template.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse {
    private Integer lastVisiblePage;

    private Boolean hasNextPage;

    private Integer currentPage;

    private PaginationItems paginationItems;
}
