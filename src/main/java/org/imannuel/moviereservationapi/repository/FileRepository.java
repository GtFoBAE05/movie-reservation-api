package org.imannuel.moviereservationapi.repository;

import org.imannuel.moviereservationapi.entity.File;
import org.imannuel.moviereservationapi.entity.MovieImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "INSERT INTO m_file(id, content_type, filename, path, size) " +
            "values (:#{#movieImage.id}, :#{#movieImage.contentType}, :#{#movieImage.filename}, " +
            ":#{#movieImage.path}, :#{#movieImage.size})", nativeQuery = true)
    void insertFile(@Param("movieImage") MovieImage movieImage);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "UPDATE m_file " +
            "SET content_type = :#{#movieImage.contentType}, " +
            "filename = :#{#movieImage.filename}, " +
            "path = :#{#movieImage.path}, " +
            "size = :#{#movieImage.size} " +
            "WHERE id = :#{#movieImage.id}", nativeQuery = true)
    void updateFile(@Param("movieImage") MovieImage movieImage);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "DELETE from m_file " +
            "WHERE id = :id", nativeQuery = true)
    void deleteMovieImage(@Param("id") UUID id);
}
