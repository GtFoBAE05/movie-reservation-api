package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.dto.response.movie.MovieListResponse;
import org.imannuel.moviereservationapi.dto.response.movie.MovieResponse;
import org.imannuel.moviereservationapi.entity.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MovieService {
    void insertMovie(List<MultipartFile> multipartFiles, String title, String description,
                     Integer durationInMinute, String genres, String releaseDate);

    void insertMovieGenre(UUID movieId, Long genreId);

    void updateMovieById(String id, List<MultipartFile> multipartFiles, String title, String description,
                         Integer durationInMinute, String genres, String releaseDate);

    Movie findMovieById(String id);

    MovieResponse getMovieById(String id);

    MovieListResponse searchMovie(String title);

    void deleteMovieById(String id);

    void updateMovieImage(String imageId, MultipartFile file);

    void deleteMovieImage(String id);
}
