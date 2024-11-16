package org.imannuel.moviereservationapi.dto.response.template.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationItems {
    private Integer count;

    private Long total;

    private Integer perPage;
}
