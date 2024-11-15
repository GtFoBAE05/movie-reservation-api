package org.imannuel.moviereservationapi.controller;

import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.genre.GenreRequest;
import org.imannuel.moviereservationapi.dto.response.genre.GenreListResponse;
import org.imannuel.moviereservationapi.dto.response.genre.GenreResponse;
import org.imannuel.moviereservationapi.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createGenre(
            @RequestBody GenreRequest genreRequest
    ) {
        genreService.createGenre(genreRequest);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Success create genre", null);
    }

    @GetMapping
    public ResponseEntity getAllGenre() {
        GenreListResponse genres = genreService.getAllGenre();
        return ApiMapper.basicMapper(HttpStatus.OK, "Success get all genre", genres);
    }

    @GetMapping("/{id}")
    public ResponseEntity getGenreById(
            @PathVariable(name = "id") Long id
    ) {
        GenreResponse genre = genreService.getGenreById(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success get genre with id " + id, genre);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateGenre(
            @PathVariable(name = "id") Long id,
            @RequestBody GenreRequest genreRequest
    ) {
        genreService.updateGenre(id, genreRequest);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success update genre", null);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteGenre(
            @PathVariable(name = "id") Long id
    ) {
        genreService.deleteGenre(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success delete genre with id " + id, null);
    }
}
