package org.imannuel.moviereservationapi.dto.mapper;

import org.imannuel.moviereservationapi.dto.response.genre.GenreResponse;
import org.imannuel.moviereservationapi.dto.response.movie.MovieResponse;
import org.imannuel.moviereservationapi.entity.Movie;

import java.util.List;

public class MovieMapper {
    public static MovieResponse movieToMovieResponse(Movie movie) {
        List<GenreResponse> genreResponses = movie.getGenres().stream().map(
                GenreMapper::genreToGenreResponse
        ).toList();
        return MovieResponse.builder()
                .id(movie.getId().toString())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .durationInMinute(movie.getDurationInMinutes())
                .genres(genreResponses)
                .posterImage(movie.getImages().stream().map(movieImage -> movieImage.getId().toString()).toList())
                .releaseDate(movie.getReleaseDate().toString())
                .build();
    }

    public static List<MovieResponse> movieListToMovieListResponse(List<Movie> movies) {
        return movies.stream().map(
                MovieMapper::movieToMovieResponse
        ).toList();
    }
}
