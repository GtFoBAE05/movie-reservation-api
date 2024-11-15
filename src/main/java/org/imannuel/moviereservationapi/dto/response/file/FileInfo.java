package org.imannuel.moviereservationapi.dto.response.file;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileInfo {
    private String filename;
    private String path;
}

