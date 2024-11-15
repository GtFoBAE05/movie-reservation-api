package org.imannuel.moviereservationapi.controller;

import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.response.file.FileDownloadResponse;
import org.imannuel.moviereservationapi.service.MovieImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/files")
@RequiredArgsConstructor
public class FileController {
    private final MovieImageService movieImageService;

    @GetMapping(path = "/images/{id}")
    public ResponseEntity<?> downloadImage(@PathVariable String id) {
        FileDownloadResponse response = movieImageService.downloadMovieImage(id);
        String headerValue = String.format("inline; filename=%s", response.getResource().getFilename());
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .contentType(MediaType.valueOf(response.getContentType()))
                .body(response.getResource());
    }

}
