package org.imannuel.moviereservationapi.dto.mapper.template;

import org.imannuel.moviereservationapi.dto.response.template.ApiTemplateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiMapper {
    public static <T> ResponseEntity<ApiTemplateResponse> basicMapper(HttpStatus httpStatus, String message, T data) {
        return ResponseEntity.status(httpStatus).body(
                ApiTemplateResponse.builder()
                        .httpStatus(httpStatus.value())
                        .message(message)
                        .data(data)
                        .build()
        );
    }
}
