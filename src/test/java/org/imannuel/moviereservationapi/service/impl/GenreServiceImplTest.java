package org.imannuel.moviereservationapi.service.impl;

import org.imannuel.moviereservationapi.dto.mapper.GenreMapper;
import org.imannuel.moviereservationapi.dto.request.genre.GenreRequest;
import org.imannuel.moviereservationapi.dto.response.genre.GenrePageResponse;
import org.imannuel.moviereservationapi.dto.response.genre.GenreResponse;
import org.imannuel.moviereservationapi.entity.Genre;
import org.imannuel.moviereservationapi.repository.GenreRepository;
import org.imannuel.moviereservationapi.utils.PaginationUtil;
import org.imannuel.moviereservationapi.utils.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {
    @Mock
    private GenreRepository genreRepository;

    @Mock
    private ValidationUtil validationUtil;

    @InjectMocks
    private GenreServiceImpl genreService;

    @Test
    void shoulCallInsertGenreWhenInsertGenre() {
        String genreName = "ACTION";

        genreRepository.insertGenre(genreName);

        Mockito.verify(genreRepository,
                Mockito.times(1)).insertGenre(genreName);
    }

    @Test
    void shouldCallInsertGenreWhenCreateGenre() {
        String genreName = "ACTION";
        GenreRequest genreRequest = GenreRequest.builder()
                .name(genreName)
                .build();
        Mockito.doNothing().when(validationUtil).validate(genreRequest);

        Mockito.when(genreRepository.existsGenreByName(genreRequest.getName())).thenReturn(false);
        genreService.createGenre(genreRequest);

        Mockito.verify(validationUtil, Mockito.times(1))
                .validate(genreRequest);
        Mockito.verify(genreRepository, Mockito.times(1))
                .insertGenre(genreName);
    }

    @Test
    void shouldThrowErrorWhenCreateGenre() {
        String genreName = "ACTION";
        GenreRequest genreRequest = GenreRequest.builder()
                .name(genreName)
                .build();

        Mockito.doNothing().when(validationUtil).validate(genreRequest);
        Mockito.when(genreRepository.existsGenreByName(genreRequest.getName())).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> genreService.createGenre(genreRequest)
        );

        assertEquals("Genre already exists", exception.getReason());
        Mockito.verify(validationUtil, Mockito.times(1))
                .validate(genreRequest);
        Mockito.verify(genreRepository, Mockito.times(1))
                .existsGenreByName(genreName);
    }

    @Test
    void shouldReturnGenreWhenFindGenreById() {
        Long id = 1L;
        Genre expectedGenre = Genre.builder()
                .id(id)
                .name("ACTION")
                .build();

        Mockito.when(genreRepository.findGenreById(id)).thenReturn(Optional.of(expectedGenre));

        Genre foundGenre = genreService.findGenreById(id);

        assertEquals(expectedGenre.getId(), foundGenre.getId());
        assertEquals(expectedGenre.getName(), foundGenre.getName());
        Mockito.verify(genreRepository, Mockito.times(1))
                .findGenreById(id);
    }

    @Test
    void shouldThrowErrorWhenFindGenreById() {
        Long id = 99L;
        Mockito.when(genreRepository.findGenreById(id)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> genreService.findGenreById(id));

        assertEquals("Genre is not exists", exception.getReason());
        Mockito.verify(genreRepository, Mockito.times(1))
                .findGenreById(id);
    }

    @Test
    void shouldReturnGenreResponseWhenGetGenreById() {
        Long id = 1L;
        GenreResponse expectedGenreResponse = GenreResponse.builder()
                .id(id)
                .name("ACTION")
                .build();
        Genre expectedGenre = Genre.builder()
                .id(id)
                .name("ACTION")
                .build();
        Mockito.when(genreRepository.findGenreById(id)).thenReturn(Optional.of(expectedGenre));

        GenreResponse genre = genreService.getGenreById(id);

        assertEquals(expectedGenreResponse.getId(), genre.getId());
        assertEquals(expectedGenreResponse.getName(), genre.getName());
        Mockito.verify(genreRepository, Mockito.times(1))
                .findGenreById(id);
    }

    @Test
    void shouldReturnAllGenreWhenGetAllGenre() {
        int page = 0;
        int size = 10;
        int offset = PaginationUtil.calculateOffset(page, size);
        long totalElements = 1L;
        int totalPages = PaginationUtil.calculateTotalPages(totalElements, size);
        
        List<Genre> genresList = List.of(Genre.builder()
                .id(1L)
                .name("ACTION")
                .build());
        GenrePageResponse expectedGenrePageResponse = GenrePageResponse.builder()
                .genres(GenreMapper.genreListToListGenreResponse(genresList))
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();

        Mockito.when(genreRepository.getAllGenre(size, offset)).thenReturn(genresList);
        Mockito.when(genreRepository.countTotalGenres()).thenReturn(totalElements);

        GenrePageResponse allGenre = genreService.getAllGenre(page, size);
        assertEquals(expectedGenrePageResponse.getGenres().size(), allGenre.getGenres().size());
        assertEquals(expectedGenrePageResponse.getTotalElements(), allGenre.getTotalElements());
        assertEquals(expectedGenrePageResponse.getTotalPages(), allGenre.getTotalPages());
        assertEquals(expectedGenrePageResponse.getCurrentPage(), allGenre.getCurrentPage());
        assertEquals(expectedGenrePageResponse.getPageSize(), allGenre.getPageSize());

        Mockito.verify(genreRepository, Mockito.times(1)).getAllGenre(size, offset);
        Mockito.verify(genreRepository, Mockito.times(1)).countTotalGenres();
    }

    @Test
    void shouldCallUpdateGenreWhenUpdateGenre() {
        long id = 1L;
        GenreRequest genreRequest = GenreRequest.builder()
                .name("ROMANCE")
                .build();
        Mockito.doNothing().when(validationUtil).validate(genreRequest);
        Mockito.doNothing().when(genreRepository).updateGenreById(id, genreRequest.getName());

        genreService.updateGenre(id, genreRequest);

        Mockito.verify(validationUtil, Mockito.times(1)).validate(genreRequest);
        Mockito.verify(genreRepository, Mockito.times(1)).updateGenreById(id, genreRequest.getName());
    }

    @Test
    void shouldCallDeleteGenreWhenDeleteGenre() {
        long id = 1L;
        Genre genre = Genre.builder()
                .id(id)
                .name("ACTION")
                .build();
        Mockito.when(genreRepository.findGenreById(id)).thenReturn(Optional.of(genre));
        Mockito.doNothing().when(genreRepository).deleteGenre(id);
        Mockito.doNothing().when(genreRepository).deleteGenreById(id);

        genreService.deleteGenre(id);

        Mockito.verify(genreRepository, Mockito.times(1)).deleteGenre(id);
        Mockito.verify(genreRepository, Mockito.times(1)).deleteGenreById(id);
    }
}