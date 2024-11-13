package org.imannuel.moviereservationapi.dto.response.template;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private Integer httpStatus;

    private String message;

    private T data;
}
