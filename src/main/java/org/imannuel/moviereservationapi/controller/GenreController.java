package org.imannuel.moviereservationapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.genre.GenreRequest;
import org.imannuel.moviereservationapi.dto.response.genre.GenrePageResponse;
import org.imannuel.moviereservationapi.dto.response.genre.GenreResponse;
import org.imannuel.moviereservationapi.dto.response.template.ApiTemplateResponse;
import org.imannuel.moviereservationapi.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/genres")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Genre", description = "API for managing movie genres.")
public class GenreController {
    private final GenreService genreService;

    @Operation(
            summary = "Create a new genre",
            description = "Create a new genre in the system. Only accessible by users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created genre", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createGenre(
            @RequestBody GenreRequest genreRequest
    ) {
        genreService.createGenre(genreRequest);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Successfully created genre", null);
    }

    @Operation(
            summary = "Retrieve all genres",
            description = "Retrieve a list of all genres in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved genres", content = @Content(schema = @Schema(implementation = ListGenreDataResponse.class))),
            }
    )
    @GetMapping
    public ResponseEntity getAllGenre(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        GenrePageResponse genrePageResponse = genreService.getAllGenre(page, size);
        return ApiMapper.paginationMapper(HttpStatus.OK, "Successfully retrieved all genres", genrePageResponse.getGenres(),
                genrePageResponse.getTotalElements(), genrePageResponse.getPageSize(), genrePageResponse.getCurrentPage());
    }

    @Operation(
            summary = "Get genre by ID",
            description = "Retrieve details of a specific genre by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Genre details retrieved successfully", content = @Content(schema = @Schema(implementation = GenreDataResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Genre not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity getGenreById(
            @PathVariable(name = "id") Long id
    ) {
        GenreResponse genre = genreService.getGenreById(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully retrieved genre with ID " + id, genre);
    }

    @Operation(
            summary = "Update genre by ID",
            description = "Update the details of an existing genre by its ID. Only accessible by users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Genre updated successfully", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Genre not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateGenre(
            @PathVariable(name = "id") Long id,
            @RequestBody GenreRequest genreRequest
    ) {
        genreService.updateGenre(id, genreRequest);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully updated genre", null);
    }

    @Operation(
            summary = "Delete genre by ID",
            description = "Delete a specific genre by its ID. Only accessible by users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Genre deleted successfully", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Genre not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteGenre(
            @PathVariable(name = "id") Long id
    ) {
        genreService.deleteGenre(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully deleted genre with ID " + id, null);
    }

    private static class ListGenreDataResponse extends ApiTemplateResponse<GenrePageResponse> {
    }

    private static class GenreDataResponse extends ApiTemplateResponse<GenreResponse> {
    }
}
