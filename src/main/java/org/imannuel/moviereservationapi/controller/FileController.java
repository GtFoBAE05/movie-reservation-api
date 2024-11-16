package org.imannuel.moviereservationapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.response.file.FileDownloadResponse;
import org.imannuel.moviereservationapi.dto.response.template.ApiTemplateResponse;
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
@Tag(name = "File", description = "APIs for file management, including image downloads for movies.")
public class FileController {
    private final MovieImageService movieImageService;

    @Operation(
            summary = "Download an image",
            description = "Download an image associated with a movie by its ID. Returns the image file in response if found.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Image downloaded successfully", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)),
                    @ApiResponse(responseCode = "404", description = "Image not found", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
            }
    )
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
