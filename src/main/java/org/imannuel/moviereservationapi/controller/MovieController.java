package org.imannuel.moviereservationapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.response.movie.MoviePageResponse;
import org.imannuel.moviereservationapi.dto.response.movie.MovieResponse;
import org.imannuel.moviereservationapi.dto.response.template.ApiTemplateResponse;
import org.imannuel.moviereservationapi.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/api/movies")
@RequiredArgsConstructor
@Tag(name = "Movie", description = "API for managing movies in the system.")
public class MovieController {
    private final MovieService movieService;

    @Operation(
            summary = "Create a new movie",
            description = "Create a new movie entry in the system. Only accessible by users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created movie", content = @Content(schema = @Schema(implementation = MovieWithoutDataResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createMovie(
            @RequestParam(name = "images", required = false) List<MultipartFile> multipartFiles,
            @Valid @NotBlank(message = "title is required") @RequestParam(name = "title") String title,
            @Valid @NotBlank(message = "description is required") @RequestParam(name = "description") String description,
            @Valid @NotBlank(message = "durationInMinute is required") @RequestParam(name = "durationInMinute") Integer durationInMinute,
            @Valid @NotBlank(message = "genres is required") @RequestParam(name = "genres") List<Long>  genres,
            @Valid @NotBlank(message = "releaseDate is required") @RequestParam(name = "releaseDate") String releaseDate
    ) {
        movieService.insertMovie(multipartFiles, title, description, durationInMinute, genres, releaseDate);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Successfully created movie", null);
    }

    @Operation(
            summary = "Search movies",
            description = "Retrieve a list of movies based on the provided title. Returns all movies if no title is provided.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved movies", content = @Content(schema = @Schema(implementation = ListMovieDataResponse.class))),
            }
    )
    @GetMapping
    public ResponseEntity searchMovie(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        MoviePageResponse moviePageResponse = movieService.searchMovie(title, page, size);
        return ApiMapper.paginationMapper(HttpStatus.OK, "Successfully retrieved movies",
                moviePageResponse.getMovies(), moviePageResponse.getTotalElements(),
                moviePageResponse.getPageSize(), moviePageResponse.getCurrentPage());
    }

    @Operation(
            summary = "Get movie by ID",
            description = "Retrieve the details of a specific movie by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved movie details", content = @Content(schema = @Schema(implementation = MovieDataResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity getMovieById(
            @PathVariable("id") String id
    ) {
        MovieResponse movie = movieService.getMovieById(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully retrieved movie with ID " + id, movie);
    }

    @Operation(
            summary = "Update movie by ID",
            description = "Update the details of an existing movie by its ID. Only accessible by users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated movie", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class)))
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateMovie(
            @PathVariable("id") String id,
            @RequestParam(name = "images", required = false) List<MultipartFile> multipartFiles,
            @Valid @NotBlank(message = "title is required") @RequestParam(name = "title") String title,
            @Valid @NotBlank(message = "description is required") @RequestParam(name = "description") String description,
            @Valid @NotBlank(message = "durationInMinute is required") @RequestParam(name = "durationInMinute") Integer durationInMinute,
            @Valid @NotBlank(message = "genres is required") @RequestParam(name = "genres") List<Long>  genres,
            @Valid @NotBlank(message = "releaseDate is required") @RequestParam(name = "releaseDate") String releaseDate
    ) {
        movieService.updateMovieById(id, multipartFiles, title, description, durationInMinute, genres, releaseDate);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully updated movie", null);
    }

    @Operation(
            summary = "Update movie image",
            description = "Update the image(s) of an existing movie. Only accessible by users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated movie image", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
            }
    )
    @PutMapping("/images/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateMovieImage(
            @PathVariable("id") String id,
            @RequestParam(name = "images", required = false) MultipartFile multipartFile
    ) {
        movieService.updateMovieImage(id, multipartFile);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully updated movie image", null);
    }

    @Operation(
            summary = "Delete movie by ID",
            description = "Delete a specific movie by its ID. Only accessible by users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully deleted movie", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteMovie(
            @PathVariable("id") String id
    ) {
        movieService.deleteMovieById(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully deleted movie with ID " + id, null);
    }

    @Operation(
            summary = "Delete movie image",
            description = "Delete the image(s) of a movie by its ID. Only accessible by users with the ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully deleted movie image", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
            }
    )
    @DeleteMapping("/images/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteMovieImage(
            @PathVariable("id") String id
    ) {
        movieService.deleteMovieImage(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Successfully deleted movie image for movie with ID " + id, null);
    }

    private static class MovieWithoutDataResponse extends ApiTemplateResponse {
    }

    private static class ListMovieDataResponse extends ApiTemplateResponse<List<MovieResponse>> {
    }

    private static class MovieDataResponse extends ApiTemplateResponse<MovieResponse> {
    }
}
