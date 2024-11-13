package org.imannuel.moviereservationapi.dto.mapper;

import org.imannuel.moviereservationapi.dto.request.genre.GenreRequest;
import org.imannuel.moviereservationapi.dto.response.genre.GenreListResponse;
import org.imannuel.moviereservationapi.dto.response.genre.GenreResponse;
import org.imannuel.moviereservationapi.entity.Genre;

import java.util.List;

public class GenreMapper {
    public static Genre createGenreRequestToGenre(GenreRequest genreRequest){
        return Genre.builder()
                .name(genreRequest.getName())
                .build();
    }

    public static GenreResponse genreToGenreResponse(Genre genre){
        return GenreResponse.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public static GenreListResponse genreListToGenreResponse(List<Genre> genres){
        List<GenreResponse> genreResponses = genres.stream().map(genre -> genreToGenreResponse(genre)).toList();
        return GenreListResponse.builder()
                .genres(genreResponses)
                .build();
    }
}
