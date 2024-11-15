package org.imannuel.moviereservationapi.service.impl;

import jakarta.annotation.PostConstruct;
import org.imannuel.moviereservationapi.constant.FileType;
import org.imannuel.moviereservationapi.dto.response.file.FileInfo;
import org.imannuel.moviereservationapi.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;

import static org.imannuel.moviereservationapi.constant.Constant.contentTypes;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Integer MAX_SIZE;
    private final Path directoryPath;

    @Autowired
    public FileStorageServiceImpl(
            @Value("${movie.reservation.root-path-directory}") String directoryPath,
            @Value("${movie.reservation.file-max-size}") Integer maxSize
    ) {
        MAX_SIZE = maxSize;
        this.directoryPath = Paths.get(directoryPath).normalize();
    }

    @PostConstruct
    public void initDirectory() {
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
                Files.setPosixFilePermissions(directoryPath, PosixFilePermissions.fromString("rwxr-xr-x"));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while initialize directory");
            }
        }
    }

    @Override
    public FileInfo storeFile(FileType fileType, String prefixDirectory, MultipartFile multipartFile) {
        try {
            validate(multipartFile);
            String prefix = fileType.equals(FileType.FILE) ? "files" : "images";

            String filename = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();

            Path dirPath = directoryPath.resolve(prefix).normalize();
            Path newDirPath = dirPath.resolve(prefixDirectory).normalize();

            if (!Files.exists(newDirPath)) {
                Files.createDirectories(newDirPath);
            }

            Path filePath = newDirPath.resolve(filename).normalize();
            Files.copy(multipartFile.getInputStream(), filePath);
            Files.setPosixFilePermissions(filePath, PosixFilePermissions.fromString("rw-r--r--"));

            Path savedPath = Paths.get(prefix).resolve(prefixDirectory).resolve(filename).normalize();

            return FileInfo.builder()
                    .filename(filename)
                    .path(savedPath.toString())
                    .build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while save image");
        }
    }

    @Override
    public Resource readFile(String path) {
        try {
            Path filePath = directoryPath.resolve(path);
            if (!Files.exists(filePath)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
            return new UrlResource(filePath.toUri());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void deleteFile(String path) {
        try {
            Path filePath = directoryPath.resolve(path);
            if (!Files.exists(filePath)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
            Files.delete(filePath);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void validate(MultipartFile multipartFile) {
        if (multipartFile.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image cannot be empty");

        if (multipartFile.getOriginalFilename() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File name cannot be empty");

        if (multipartFile.getSize() > MAX_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File size exceed limit");
        }

        if (!contentTypes.contains(multipartFile.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid extensions type");
        }
    }

}
