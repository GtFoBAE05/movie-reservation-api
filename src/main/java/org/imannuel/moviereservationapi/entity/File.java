package org.imannuel.moviereservationapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.imannuel.moviereservationapi.constant.Constant;

import java.util.UUID;

@Entity
@Table(name = Constant.FILE_TABLE)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class File {
    @Id
    private UUID id;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "size", nullable = false)
    private long size;
}

