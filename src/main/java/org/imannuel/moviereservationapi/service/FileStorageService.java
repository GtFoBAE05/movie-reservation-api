package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.constant.FileType;
import org.imannuel.moviereservationapi.dto.response.file.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    FileInfo storeFile(FileType fileType, String prefixDirectory, MultipartFile multipartFile);

    Resource readFile(String path);

    void deleteFile(String path);
}
