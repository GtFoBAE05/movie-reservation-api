package org.imannuel.moviereservationapi.dto.mapper;

import org.imannuel.moviereservationapi.dto.request.genre.GenreRequest;
import org.imannuel.moviereservationapi.dto.response.genre.GenreResponse;
import org.imannuel.moviereservationapi.entity.Genre;

import java.util.List;

public class GenreMapper {
    public static Genre createGenreRequestToGenre(GenreRequest genreRequest) {
        return Genre.builder()
                .name(genreRequest.getName())
                .build();
    }

    public static GenreResponse genreToGenreResponse(Genre genre) {
        return GenreResponse.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public static List<GenreResponse> genreListToListGenreResponse(List<Genre> genres) {
        return genres.stream()
                .map(GenreMapper::genreToGenreResponse)
                .toList();
    }
}
