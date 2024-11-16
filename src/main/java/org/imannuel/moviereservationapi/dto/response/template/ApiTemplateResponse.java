package org.imannuel.moviereservationapi.dto.response.template;

import lombok.*;
import org.imannuel.moviereservationapi.dto.response.template.pagination.PaginationResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiTemplateResponse<T> {
    private Integer httpStatus;

    private String message;

    private T data;

    private PaginationResponse pagination;
}
