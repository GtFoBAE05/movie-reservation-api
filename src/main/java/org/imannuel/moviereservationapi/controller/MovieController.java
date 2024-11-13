package org.imannuel.moviereservationapi.controller;

import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.movie.MovieRequest;
import org.imannuel.moviereservationapi.dto.response.movie.MovieListResponse;
import org.imannuel.moviereservationapi.dto.response.movie.MovieResponse;
import org.imannuel.moviereservationapi.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping
    public ResponseEntity createMovie(
            @RequestBody MovieRequest movieRequest
    ) {
        movieService.insertMovie(movieRequest);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Success create movie", null);
    }

    @GetMapping()
    public ResponseEntity searchMovie(
            @RequestParam(value = "title", required = false) String title
    ) {
        MovieListResponse movieListResponse = movieService.searchMovie(title);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success fetch all movie", movieListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity getMovieById(
            @PathVariable("id") String id
    ) {
        MovieResponse movie = movieService.getMovieById(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success get movie", movie);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateMovie(
            @PathVariable("id") String id,
            @RequestBody MovieRequest movieRequest
    ) {
        movieService.updateMovieById(id, movieRequest);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success update movie", null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMovie(
            @PathVariable("id") String id
    ) {
        movieService.deleteMovieById(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success delete movie", null);
    }
}
