package org.imannuel.moviereservationapi.service.impl;

import org.imannuel.moviereservationapi.dto.mapper.MovieMapper;
import org.imannuel.moviereservationapi.dto.response.movie.MoviePageResponse;
import org.imannuel.moviereservationapi.dto.response.movie.MovieResponse;
import org.imannuel.moviereservationapi.entity.Genre;
import org.imannuel.moviereservationapi.entity.Movie;
import org.imannuel.moviereservationapi.entity.MovieImage;
import org.imannuel.moviereservationapi.repository.MovieRepository;
import org.imannuel.moviereservationapi.service.GenreService;
import org.imannuel.moviereservationapi.service.MovieImageService;
import org.imannuel.moviereservationapi.utils.DateUtil;
import org.imannuel.moviereservationapi.utils.PaginationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {
    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieImageService movieImageService;

    @Mock
    private GenreService genreService;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private MovieServiceImpl movieService;

    @Test
    void shouldCallInsertMovieWhenInsertMovie() {
        Movie movie = Movie.builder()
                .id(UUID.randomUUID())
                .title("title")
                .description("description")
                .durationInMinutes(15)
                .releaseDate(DateUtil.stringToLocalDate("2024-01-01"))
                .build();
        Long genreId = 1L;
        Genre genre = Genre.builder()
                .id(genreId)
                .name("ACTION")
                .build();
        Mockito.doNothing().when(movieRepository).insertMovie(Mockito.any(Movie.class));
        Mockito.when(genreService.findGenreById(genreId)).thenReturn(genre);

        movieService.insertMovie(
                List.of(file),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDurationInMinutes(),
                List.of(genreId),
                "2024-01-01"
        );

        Mockito.verify(genreService, Mockito.times(1)).findGenreById(genreId);
        Mockito.verify(movieRepository, Mockito.times(1)).insertMovieGenre(Mockito.any(UUID.class), Mockito.anyLong());
    }

    @Test
    void shouldReturnMovieWhenFindMovieById() {
        Movie expectedMovie = Movie.builder()
                .id(UUID.randomUUID())
                .title("title")
                .description("description")
                .durationInMinutes(15)
                .releaseDate(DateUtil.stringToLocalDate("2024-01-01"))
                .build();
        Mockito.when(movieRepository.findMovieById(expectedMovie.getId())).thenReturn(Optional.of(expectedMovie));

        Movie movie = movieService.findMovieById(expectedMovie.getId().toString());

        assertEquals(expectedMovie.getId(), movie.getId());
        assertEquals(expectedMovie.getTitle(), movie.getTitle());
        assertEquals(expectedMovie.getDescription(), movie.getDescription());
        assertEquals(expectedMovie.getDurationInMinutes(), movie.getDurationInMinutes());
        assertEquals(expectedMovie.getReleaseDate(), movie.getReleaseDate());

        Mockito.verify(movieRepository, Mockito.times(1)).findMovieById(movie.getId());
    }

    @Test
    void shouldThrowErrorWhenFindMovieById() {
        UUID movieId = UUID.randomUUID();
        Mockito.when(movieRepository.findMovieById(movieId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> movieService.findMovieById(movieId.toString()));

        assertEquals("Movie not found", exception.getReason());
        Mockito.verify(movieRepository, Mockito.times(1)).findMovieById(movieId);
    }

    @Test
    void shouldReturnMovieResponseWhenGetMovieById() {
        UUID movieId = UUID.randomUUID();
        Movie expectedMovie = Movie.builder()
                .id(movieId)
                .title("title")
                .description("description")
                .durationInMinutes(15)
                .releaseDate(DateUtil.stringToLocalDate("2024-01-01"))
                .genres(List.of())
                .images(List.of())
                .build();
        Mockito.when(movieRepository.findMovieById(expectedMovie.getId())).thenReturn(Optional.of(expectedMovie));

        MovieResponse movie = movieService.getMovieById(movieId.toString());

        assertEquals(expectedMovie.getId().toString(), movie.getId());
        assertEquals(expectedMovie.getTitle(), movie.getTitle());
        assertEquals(expectedMovie.getDescription(), movie.getDescription());
        assertEquals(expectedMovie.getDurationInMinutes(), movie.getDurationInMinute());
        assertEquals(expectedMovie.getReleaseDate().toString(), movie.getReleaseDate());
        Mockito.verify(movieRepository, Mockito.times(1)).findMovieById(movieId);
    }

    @Test
    void shouldReturnMoviePageResponseWhenSearchMovie() {
        int page = 0;
        int size = 10;
        int offset = PaginationUtil.calculateOffset(page, size);
        long totalElements = 1L;
        int totalPages = PaginationUtil.calculateTotalPages(totalElements, size);

        List<Movie> movieList = List.of(Movie.builder()
                .id(UUID.randomUUID())
                .title("title")
                .description("description")
                .durationInMinutes(15)
                .releaseDate(DateUtil.stringToLocalDate("2024-01-01"))
                .genres(List.of())
                .images(List.of())
                .build());
        MoviePageResponse expectedMoviePageResponse = MoviePageResponse.builder()
                .movies(MovieMapper.movieListToMovieListResponse(movieList))
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();

        Mockito.when(movieRepository.getAllMovie("title", size, offset)).thenReturn(movieList);
        Mockito.when(movieRepository.countTotalMovies()).thenReturn(totalElements);

        MoviePageResponse moviePageResponse = movieService.searchMovie("title", page, size);
        assertEquals(expectedMoviePageResponse.getMovies().size(), moviePageResponse.getMovies().size());
        assertEquals(expectedMoviePageResponse.getTotalElements(), moviePageResponse.getTotalElements());
        assertEquals(expectedMoviePageResponse.getTotalPages(), moviePageResponse.getTotalPages());
        assertEquals(expectedMoviePageResponse.getCurrentPage(), moviePageResponse.getCurrentPage());
        assertEquals(expectedMoviePageResponse.getPageSize(), moviePageResponse.getPageSize());

        Mockito.verify(movieRepository, Mockito.times(1)).getAllMovie("title", size, offset);
        Mockito.verify(movieRepository, Mockito.times(1)).countTotalMovies();
    }

    @Test
    void shouldCallDeleteMovieWhenDeleteMovieById() {
        UUID movieId = UUID.randomUUID();
        Movie movie = Movie.builder()
                .id(movieId)
                .title("title")
                .description("description")
                .durationInMinutes(15)
                .releaseDate(DateUtil.stringToLocalDate("2024-01-01"))
                .genres(List.of())
                .images(List.of())
                .build();
        movie.setImages(List.of(MovieImage.builder().movie(movie).build()));

        Mockito.when(movieRepository.findMovieById(movieId)).thenReturn(Optional.of(movie));
        Mockito.doNothing().when(movieRepository).deleteMovieById(movieId);

        movieService.deleteMovieById(movieId.toString());

        Mockito.verify(movieRepository, Mockito.times(1)).deleteMovieById(movieId);
    }

    @Test
    void shouldUpdateMovieImageWhenUpdateMovieImage() {
        String imageId = "image123";
        Mockito.doNothing().when(movieImageService).updateMovieImage(imageId, file);

        movieService.updateMovieImage(imageId, file);

        Mockito.verify(movieImageService).updateMovieImage(Mockito.eq(imageId), Mockito.eq(file));
    }
}