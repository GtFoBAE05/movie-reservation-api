package org.imannuel.moviereservationapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.moviereservationapi.dto.mapper.MovieMapper;
import org.imannuel.moviereservationapi.dto.response.movie.MovieListResponse;
import org.imannuel.moviereservationapi.dto.response.movie.MovieResponse;
import org.imannuel.moviereservationapi.entity.Movie;
import org.imannuel.moviereservationapi.entity.MovieImage;
import org.imannuel.moviereservationapi.repository.MovieRepository;
import org.imannuel.moviereservationapi.service.GenreService;
import org.imannuel.moviereservationapi.service.MovieImageService;
import org.imannuel.moviereservationapi.service.MovieService;
import org.imannuel.moviereservationapi.utils.DateParse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieImageService movieImageService;
    private final GenreService genreService;

//    @PostConstruct
//    @Transactional
//    public void init() {
//        GenreListResponse allGenre = genreService.getAllGenre();
//
//        SeedData.movieSeedData.stream()
//                .filter(movieRequest -> !movieRepository.existsMovieByName(movieRequest.getTitle()))
//                .peek(movieRequest -> {
//                    GenreResponse randomGenre = allGenre.getGenres().get(new Random().nextInt(allGenre.getGenres().size()));
//                    movieRequest.setGenres(List.of(randomGenre.getId()));
//                })
//                .forEach(movieRequest -> insertMovie(movieRequest));
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertMovie(List<MultipartFile> multipartFiles, String title, String description,
                            Integer durationInMinute, String genres, String releaseDate) {
        Movie movie = Movie.builder()
                .id(UUID.randomUUID())
                .title(title)
                .description(description)
                .durationInMinutes(durationInMinute)
                .releaseDate(DateParse.stringToLocalDate(releaseDate))
                .build();
        movieRepository.insertMovie(movie);

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            List<MovieImage> menuImages = movieImageService.createMovieImageBulk(multipartFiles, movie);
            movie.setImages(menuImages);
        }

        Arrays.stream(genres.split(",")).toList().forEach(aLong -> {
            genreService.findGenreById(Long.parseLong(aLong));
            insertMovieGenre(movie.getId(), Long.parseLong(aLong));
        });
    }

    @Override
    public void insertMovieGenre(UUID movieId, Long genreId) {
        movieRepository.insertMovieGenre(movieId, genreId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMovieById(String id, List<MultipartFile> multipartFiles, String title, String description,
                                Integer durationInMinute, String genres, String releaseDate) {
        Movie movie = findMovieById(id);

        movie.setTitle(title);
        movie.setDescription(description);
        movie.setDurationInMinutes(durationInMinute);
        movie.setReleaseDate(DateParse.stringToLocalDate(releaseDate));
        movieRepository.updateMovieById(movie);

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            List<MovieImage> menuImages = movieImageService.createMovieImageBulk(multipartFiles, movie);
            movie.getImages().addAll(menuImages);
        }

        List<Long> genreMappedList = Arrays.stream(genres.split(",")).map(s -> Long.parseLong(s)).toList();
        movieRepository.removeGenresNotInList(movie.getId(), genreMappedList);
        movieRepository.addGenresList(movie.getId(), genreMappedList);
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
