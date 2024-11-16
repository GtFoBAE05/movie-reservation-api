package org.imannuel.moviereservationapi.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.moviereservationapi.constant.SeedData;
import org.imannuel.moviereservationapi.dto.mapper.MovieMapper;
import org.imannuel.moviereservationapi.dto.response.genre.GenrePageResponse;
import org.imannuel.moviereservationapi.dto.response.genre.GenreResponse;
import org.imannuel.moviereservationapi.dto.response.movie.MoviePageResponse;
import org.imannuel.moviereservationapi.dto.response.movie.MovieResponse;
import org.imannuel.moviereservationapi.entity.Movie;
import org.imannuel.moviereservationapi.entity.MovieImage;
import org.imannuel.moviereservationapi.repository.MovieRepository;
import org.imannuel.moviereservationapi.service.GenreService;
import org.imannuel.moviereservationapi.service.MovieImageService;
import org.imannuel.moviereservationapi.service.MovieService;
import org.imannuel.moviereservationapi.utils.DateUtil;
import org.imannuel.moviereservationapi.utils.PaginationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieImageService movieImageService;
    private final GenreService genreService;

    @PostConstruct
    @Transactional
    public void init() {
        GenrePageResponse allGenre = genreService.getAllGenre(0, 10);

        SeedData.movieSeedData.stream()
                .filter(movieRequest -> !movieRepository.existsMovieByName(movieRequest.getTitle()))
                .peek(movieRequest -> {
                    GenreResponse randomGenre = allGenre.getGenres().get(new Random().nextInt(allGenre.getGenres().size()));
                    movieRequest.setGenres(List.of(randomGenre.getId()));
                })
                .forEach(movieRequest -> insertMovie(List.of(), movieRequest.getTitle(), movieRequest.getDescription(),
                        movieRequest.getDurationInMinute(), movieRequest.getGenres(), movieRequest.getReleaseDate()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertMovie(List<MultipartFile> multipartFiles, String title, String description,
                            Integer durationInMinute, List<Long> genres, String releaseDate) {
        Movie movie = Movie.builder()
                .id(UUID.randomUUID())
                .title(title)
                .description(description)
                .durationInMinutes(durationInMinute)
                .releaseDate(DateUtil.stringToLocalDate(releaseDate))
                .build();
        movieRepository.insertMovie(movie);

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            List<MovieImage> menuImages = movieImageService.createMovieImageBulk(multipartFiles, movie);
            movie.setImages(menuImages);
        }

        genres.forEach(aLong -> {
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
    public void updateMovieById(String id, List<MultipartFile> multipartFiles, String title, String description,
                                Integer durationInMinute, List<Long> genres, String releaseDate) {
        Movie movie = findMovieById(id);

        movie.setTitle(title);
        movie.setDescription(description);
        movie.setDurationInMinutes(durationInMinute);
        movie.setReleaseDate(DateUtil.stringToLocalDate(releaseDate));
        movieRepository.updateMovieById(movie);

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            List<MovieImage> menuImages = movieImageService.createMovieImageBulk(multipartFiles, movie);
            movie.getImages().addAll(menuImages);
        }

        movieRepository.removeGenresNotInList(movie.getId(), genres);
        movieRepository.addGenresList(movie.getId(), genres);
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
    public MoviePageResponse searchMovie(String title, Integer page, Integer size) {
        int offset = PaginationUtil.calculateOffset(page, size);
        long totalMovies = movieRepository.countTotalMovies();
        int totalPages = PaginationUtil.calculateTotalPages(totalMovies, size);

        List<Movie> allMovie = movieRepository.getAllMovie(title, size, offset);

        return MoviePageResponse.builder()
                .movies(MovieMapper.movieListToMovieListResponse(allMovie))
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalMovies)
                .totalPages(totalPages)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMovieById(String id) {
        Movie movie = findMovieById(id);

        List<Long> genreList = movie.getGenres().stream()
                .map(genre -> genre.getId())
                .collect(Collectors.toList());

        if (movie.getImages() != null && !movie.getImages().isEmpty()) {
            movie.getImages().stream().forEach(
                    movieImage -> deleteMovieImage(id)
            );
        }

        movieRepository.deleteMovieGenreList(movie.getId(), genreList);
        movieRepository.deleteMovieById(UUID.fromString(id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMovieImage(String imageId, MultipartFile file) {
        movieImageService.updateMovieImage(imageId, file);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteMovieImage(String id) {
        movieImageService.deleteMovieImageById(id);
    }


}
