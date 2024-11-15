package org.imannuel.moviereservationapi.dto.response.file;


import lombok.*;
import org.springframework.core.io.Resource;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDownloadResponse {
    private Resource resource;
    private String contentType;
}
