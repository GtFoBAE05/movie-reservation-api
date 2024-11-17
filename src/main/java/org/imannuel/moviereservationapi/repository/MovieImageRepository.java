package org.imannuel.moviereservationapi.repository;

import org.imannuel.moviereservationapi.entity.MovieImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieImageRepository extends JpaRepository<MovieImage, UUID> {
    @Transactional(readOnly = true)
    @Query(value = "SELECT id, movie_id " +
            "FROM m_movie_image " +
            "WHERE movie_id = :id", nativeQuery = true)
    List<MovieImage> getAllMovieImageByMovieId(@Param("id") UUID id);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "INSERT INTO m_movie_image(id, movie_id) " +
            "values (:#{#movieImage.id}, :#{#movieImage.movie.id})", nativeQuery = true)
    void insertMovieImage(@Param("movieImage") MovieImage movieImage);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "UPDATE m_movie_image " +
            "SET movie_id = :#{#movieImage.movie.id} " +
            "WHERE id = :#{#movieImage.movie.id}", nativeQuery = true)
    void updateMovieImage(@Param("movieImage") MovieImage movieImage);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "DELETE from m_movie_image " +
            "WHERE id = :id", nativeQuery = true)
    void deleteMovieImage(@Param("id") UUID id);
}
