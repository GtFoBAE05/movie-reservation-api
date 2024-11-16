package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.dto.response.file.FileDownloadResponse;
import org.imannuel.moviereservationapi.entity.Movie;
import org.imannuel.moviereservationapi.entity.MovieImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MovieImageService {
    MovieImage createMovieImage(MultipartFile multipartFile, Movie movie);

    List<MovieImage> createMovieImageBulk(List<MultipartFile> multipartFiles, Movie movie);

    void updateMovieImage(String imageId, MultipartFile multipartFile);

    void deleteMovieImageById(String id);

    FileDownloadResponse downloadMovieImage(String id);

    MovieImage findMovieImageById(String id);
}
