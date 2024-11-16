package org.imannuel.moviereservationapi.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.constant.SeedData;
import org.imannuel.moviereservationapi.dto.mapper.GenreMapper;
import org.imannuel.moviereservationapi.dto.request.genre.GenreRequest;
import org.imannuel.moviereservationapi.dto.response.genre.GenrePageResponse;
import org.imannuel.moviereservationapi.dto.response.genre.GenreResponse;
import org.imannuel.moviereservationapi.entity.Genre;
import org.imannuel.moviereservationapi.repository.GenreRepository;
import org.imannuel.moviereservationapi.service.GenreService;
import org.imannuel.moviereservationapi.utils.PaginationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    public void initGenre() {
        SeedData.genreSeedData.forEach(s -> {
            if (!checkIsGenreExists(s)) {
                insertGenre(Genre.builder()
                        .name(s)
                        .build());
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertGenre(Genre genre) {
        genreRepository.insertGenre(genre.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createGenre(GenreRequest genreRequest) {
        if (genreRepository.existsGenreByName(genreRequest.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Genre already exists");
        }
        genreRepository.insertGenre(genreRequest.getName());
    }

    @Override
    public Genre findGenreById(Long id) {
        return genreRepository.findGenreById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre is not exists"));
    }

    @Override
    public GenreResponse getGenreById(Long id) {
        return GenreMapper.genreToGenreResponse(findGenreById(id));
    }

    @Override
    public GenrePageResponse getAllGenre(Integer page, Integer size) {
        int offset = PaginationUtil.calculateOffset(page, size);
        long totalGenres = genreRepository.countTotalGenres();
        int totalPages = PaginationUtil.calculateTotalPages(totalGenres, size);

        List<Genre> allGenre = genreRepository.getAllGenre(size, offset);

        return GenrePageResponse.builder()
                .genres(GenreMapper.genreListToListGenreResponse(allGenre))
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalGenres)
                .totalPages(totalPages)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGenre(Long id, GenreRequest genreRequest) {
        genreRepository.updateGenreById(id, genreRequest.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGenre(Long id) {
        findGenreById(id);
        genreRepository.deleteGenre(id);
        genreRepository.deleteGenreById(id);
    }

    @Override
    public boolean checkIsGenreExists(String name) {
        return genreRepository.existsGenreByName(name);
    }
}
