package org.imannuel.moviereservationapi.controller;

import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.response.movie.MovieListResponse;
import org.imannuel.moviereservationapi.dto.response.movie.MovieResponse;
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
public class MovieController {
    private final MovieService movieService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createMovie(
            @RequestParam(name = "images", required = false) List<MultipartFile> multipartFiles,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "durationInMinute") Integer durationInMinute,
            @RequestParam(name = "genres") String genres,
            @RequestParam(name = "releaseDate") String releaseDate
    ) {
        movieService.insertMovie(multipartFiles, title, description, durationInMinute, genres, releaseDate
        );
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Success create movie", null);
    }

    @GetMapping
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateMovie(
            @PathVariable("id") String id,
            @RequestParam(name = "images", required = false) List<MultipartFile> multipartFiles,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "durationInMinute") Integer durationInMinute,
            @RequestParam(name = "genres") String genres,
            @RequestParam(name = "releaseDate") String releaseDate
    ) {
        movieService.updateMovieById(id, multipartFiles, title, description, durationInMinute, genres, releaseDate);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success update movie", null);
    }

    @PutMapping("/images/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateMovieImage(
            @PathVariable("id") String id,
            @RequestParam(name = "images", required = false) MultipartFile multipartFile
    ) {
        movieService.updateMovieImage(id, multipartFile);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success update movie image", null);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteMovie(
            @PathVariable("id") String id
    ) {
        movieService.deleteMovieById(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success delete movie", null);
    }

    @DeleteMapping("/images/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteMovieImage(
            @PathVariable("id") String id
    ) {
        movieService.deleteMovieImage(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success delete movie image", null);
    }

}
