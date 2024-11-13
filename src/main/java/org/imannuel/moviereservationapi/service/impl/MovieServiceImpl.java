package org.imannuel.moviereservationapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.moviereservationapi.dto.mapper.MovieMapper;
import org.imannuel.moviereservationapi.dto.request.movie.MovieRequest;
import org.imannuel.moviereservationapi.dto.response.movie.MovieListResponse;
import org.imannuel.moviereservationapi.dto.response.movie.MovieResponse;
import org.imannuel.moviereservationapi.entity.Movie;
import org.imannuel.moviereservationapi.repository.MovieRepository;
import org.imannuel.moviereservationapi.service.GenreService;
import org.imannuel.moviereservationapi.service.MovieService;
import org.imannuel.moviereservationapi.utils.DateParse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final GenreService genreService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertMovie(MovieRequest movieRequest) {
        Movie movie = Movie.builder()
                .id(UUID.randomUUID())
                .title(movieRequest.getTitle())
                .description(movieRequest.getDescription())
                .posterImage(movieRequest.getPosterImage())
                .durationInMinutes(movieRequest.getDurationInMinute())
                .releaseDate(DateParse.stringToLocalDate(movieRequest.getReleaseDate()))
                .build();
        movieRepository.insertMovie(movie);

        movieRequest.getGenres().forEach(aLong -> {
            genreService.findGenreById(aLong);
            insertMovieGenre(movie.getId(), aLong);
        });

    }

    @Override
    public void insertMovieGenre(UUID movieId, Long genreId) {
        movieRepository.insertMovieGenre(movieId, genreId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMovieById(String id, MovieRequest movieRequest) {
        Movie movie = findMovieById(id);

        movie.setTitle(movieRequest.getTitle());
        movie.setDescription(movieRequest.getDescription());
        movie.setDurationInMinutes(movieRequest.getDurationInMinute());
        movie.setPosterImage(movieRequest.getPosterImage());
        movie.setReleaseDate(DateParse.stringToLocalDate(movieRequest.getReleaseDate()));
        movieRepository.updateMovieById(movie);

        movieRepository.removeGenresNotInList(movie.getId(), movieRequest.getGenres());
        movieRepository.addGenresList(movie.getId(), movieRequest.getGenres());
    }

    @Override
    public Movie findMovieById(String id) {
        return movieRepository.findMovieById(UUID.fromString(id)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
    }

    @Override
    public MovieResponse getMovieById(String id) {
        Movie movie = findMovieById(id);
        return MovieMapper.movieToMovieResponse(movie);
    }

    @Override
    public MovieListResponse searchMovie(String title) {
        List<Movie> allMovie = movieRepository.getAllMovie(title);
        return MovieMapper.movieListToMovieListResponse(allMovie);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMovieById(String id) {
        Movie movie = findMovieById(id);

        List<Long> genreList = movie.getGenres().stream()
                .map(genre -> genre.getId())
                .collect(Collectors.toList());

        movieRepository.deleteMovieGenreList(movie.getId(), genreList);
        movieRepository.deleteMovieById(UUID.fromString(id));
    }

}
