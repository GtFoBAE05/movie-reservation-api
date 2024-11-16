package org.imannuel.moviereservationapi.dto.mapper.template;

import org.imannuel.moviereservationapi.dto.response.template.ApiTemplateResponse;
import org.imannuel.moviereservationapi.dto.response.template.pagination.PaginationItems;
import org.imannuel.moviereservationapi.dto.response.template.pagination.PaginationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ApiMapper {
    public static <T> ResponseEntity<ApiTemplateResponse> basicMapper(HttpStatus httpStatus, String message, T data) {
        return ResponseEntity.status(httpStatus).body(
                ApiTemplateResponse.builder()
                        .httpStatus(httpStatus.value())
                        .message(message)
                        .data(data)
                        .pagination(null)
                        .build()
        );
    }

    public static <T> ResponseEntity<ApiTemplateResponse> paginationMapper(
            HttpStatus httpStatus,
            String message,
            List<T> data,
            long totalElements,
            int pageSize,
            int currentPage
    ) {
        PaginationResponse paginationResponse = paginationResponseMapper(totalElements, pageSize, currentPage);

        return ResponseEntity.status(httpStatus).body(
                ApiTemplateResponse.builder()
                        .httpStatus(httpStatus.value())
                        .message(message)
                        .data(data)
                        .pagination(paginationResponse)
                        .build()
        );
    }

    public static PaginationResponse paginationResponseMapper(long totalElements, int pageSize, int currentPage) {
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        PaginationItems paginationItems = PaginationItems.builder()
                .count(pageSize)
                .total(totalElements)
                .perPage(pageSize)
                .build();

        return PaginationResponse.builder()
                .paginationItems(paginationItems)
                .lastVisiblePage(totalPages - 1)
                .hasNextPage(currentPage < totalPages - 1)
                .currentPage(currentPage)
                .build();
    }

}
