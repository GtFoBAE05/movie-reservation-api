package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.dto.request.movie.MovieRequest;
import org.imannuel.moviereservationapi.dto.response.movie.MovieListResponse;
import org.imannuel.moviereservationapi.dto.response.movie.MovieResponse;
import org.imannuel.moviereservationapi.entity.Movie;

import java.util.UUID;

public interface MovieService {
    void insertMovie(MovieRequest movieRequest);

    void insertMovieGenre(UUID movieId, Long genreId);

    void updateMovieById(String id, MovieRequest movieRequest);

    Movie findMovieById(String id);

    MovieResponse getMovieById(String id);

    MovieListResponse searchMovie(String title);

    void deleteMovieById(String id);
}
