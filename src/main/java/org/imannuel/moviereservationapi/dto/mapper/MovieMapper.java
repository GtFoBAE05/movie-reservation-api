package org.imannuel.moviereservationapi.dto.mapper;

import org.imannuel.moviereservationapi.dto.request.movie.MovieRequest;
import org.imannuel.moviereservationapi.dto.response.genre.GenreResponse;
import org.imannuel.moviereservationapi.dto.response.movie.MovieListResponse;
import org.imannuel.moviereservationapi.dto.response.movie.MovieResponse;
import org.imannuel.moviereservationapi.entity.Movie;
import org.imannuel.moviereservationapi.utils.DateParse;

import java.util.List;

public class MovieMapper {
    public static Movie movieRequestToMovie(MovieRequest movieRequest) {
        return Movie.builder()
                .title(movieRequest.getTitle())
                .description(movieRequest.getDescription())
                .posterImage(movieRequest.getPosterImage())
                .durationInMinutes(movieRequest.getDurationInMinute())
                .releaseDate(DateParse.stringToLocalDate(movieRequest.getReleaseDate()))
                .build();
    }

    public static MovieResponse movieToMovieResponse(Movie movie) {
        List<GenreResponse> genreResponses = movie.getGenres().stream().map(
                genre -> GenreMapper.genreToGenreResponse(genre)
        ).toList();
        return MovieResponse.builder()
                .id(movie.getId().toString())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .durationInMinute(movie.getDurationInMinutes())
                .genres(genreResponses)
                .posterImage(movie.getPosterImage())
                .releaseDate(movie.getReleaseDate().toString())
                .build();
    }

    public static MovieListResponse movieListToMovieListResponse(List<Movie> movies) {
        return MovieListResponse.builder()
                .movies(movies.stream().map(movie -> movieToMovieResponse(movie)).toList())
                .build();
    }
}
