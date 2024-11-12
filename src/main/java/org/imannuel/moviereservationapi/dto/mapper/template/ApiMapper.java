package org.imannuel.moviereservationapi.dto.mapper.template;

import org.imannuel.moviereservationapi.dto.response.template.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiMapper {
    public static <T> ResponseEntity<ApiResponse> basicMapper(HttpStatus httpStatus, String message, T data) {
        return ResponseEntity.status(httpStatus).body(
                ApiResponse.builder()
                        .httpStatus(httpStatus)
                        .message(message)
                        .data(data)
                        .build()
        );
    }
}
