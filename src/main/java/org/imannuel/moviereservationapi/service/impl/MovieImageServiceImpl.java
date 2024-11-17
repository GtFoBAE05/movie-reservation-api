package org.imannuel.moviereservationapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.constant.FileType;
import org.imannuel.moviereservationapi.dto.response.file.FileDownloadResponse;
import org.imannuel.moviereservationapi.dto.response.file.FileInfo;
import org.imannuel.moviereservationapi.entity.Movie;
import org.imannuel.moviereservationapi.entity.MovieImage;
import org.imannuel.moviereservationapi.repository.FileRepository;
import org.imannuel.moviereservationapi.repository.MovieImageRepository;
import org.imannuel.moviereservationapi.service.FileStorageService;
import org.imannuel.moviereservationapi.service.MovieImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovieImageServiceImpl implements MovieImageService {
    private final MovieImageRepository movieImageRepository;
    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;
    private final String MOVIE = "movie";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MovieImage createMovieImage(MultipartFile multipartFile, Movie movie) {
        FileInfo fileInfo = fileStorageService.storeFile(FileType.IMAGE, MOVIE, multipartFile);
        MovieImage movieImage = MovieImage.builder()
                .id(UUID.randomUUID())
                .filename(fileInfo.getFilename())
                .contentType(multipartFile.getContentType())
                .size(multipartFile.getSize())
                .path(fileInfo.getPath())
                .movie(movie)
                .build();
        fileRepository.insertFile(movieImage);
        movieImageRepository.insertMovieImage(movieImage);
        return movieImage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MovieImage> createMovieImageBulk(List<MultipartFile> multipartFiles, Movie movie) {
        return multipartFiles.stream().map(multipartFile -> createMovieImage(multipartFile, movie)).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMovieImage(String imageId, MultipartFile multipartFile) {
        MovieImage movieImage = findMovieImageById(imageId);
        FileInfo fileInfo = fileStorageService.storeFile(FileType.IMAGE, MOVIE, multipartFile);
        fileStorageService.deleteFile(movieImage.getPath());
        movieImage.setPath(fileInfo.getPath());
        movieImageRepository.updateMovieImage(movieImage);
        fileRepository.updateFile(movieImage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMovieImageById(String id) {
        MovieImage movieImage = findMovieImageById(id);
        String path = movieImage.getPath();
        movieImageRepository.deleteMovieImage(movieImage.getId());
        fileRepository.deleteMovieImage(movieImage.getId());
        fileStorageService.deleteFile(path);
    }

    @Override
    @Transactional(readOnly = true)
    public FileDownloadResponse downloadMovieImage(String id) {
        MovieImage movieImage = findMovieImageById(id);
        Resource resource = fileStorageService.readFile(movieImage.getPath());
        return FileDownloadResponse.builder()
                .resource(resource)
                .contentType(movieImage.getContentType())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public MovieImage findMovieImageById(String id) {
        return movieImageRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
    }
}
