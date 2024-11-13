package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.dto.request.genre.GenreRequest;
import org.imannuel.moviereservationapi.dto.response.genre.GenreListResponse;
import org.imannuel.moviereservationapi.dto.response.genre.GenreResponse;
import org.imannuel.moviereservationapi.entity.Genre;

public interface GenreService {
    void createGenre(GenreRequest genreRequest);

    Genre findGenreById(Long id);

    GenreResponse getGenreById(Long id);

    GenreListResponse getAllGenre();

    void updateGenre(Long id, GenreRequest genreRequest);

    void deleteGenre(Long id);

    boolean checkIsGenreExists(String name);
}
