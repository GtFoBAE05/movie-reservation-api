package org.imannuel.moviereservationapi.repository;

import org.imannuel.moviereservationapi.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO  m_movie(id, title, description, duration_in_minutes, poster_image, release_date) " +
            "VALUES (:#{#movie.id}, :#{#movie.title}, :#{#movie.description}, :#{#movie.durationInMinutes}, :#{#movie.posterImage}, :#{#movie.releaseDate})", nativeQuery = true)
    void insertMovie(@Param("movie") Movie movie);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO m_movie_genre (movie_id, genre_id) " +
            "VALUES (:movieId, :genreId)", nativeQuery = true)
    void insertMovieGenre(@Param("movieId") UUID movieId, @Param("genreId") Long genreId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO m_movie_genre (movie_id, genre_id) " +
            "SELECT :movieId, mg.id FROM m_genre mg WHERE mg.id IN :genreId " +
            "AND mg.id NOT IN (SELECT genre_id FROM m_movie_genre WHERE movie_id = :movieId)", nativeQuery = true)
    void addGenresList(@Param("movieId") UUID movieId, @Param("genreId") List<Long> genreId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM m_movie_genre WHERE movie_id = :movieId AND genre_id NOT IN :genreId", nativeQuery = true)
    void removeGenresNotInList(@Param("movieId") UUID movieId, @Param("genreId") List<Long> genreId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM m_movie_genre WHERE movie_id = :movieId AND genre_id IN :genreIds", nativeQuery = true)
    void deleteMovieGenreList(@Param("movieId") UUID movieId, @Param("genreIds") List<Long> genreIds);

    @Transactional
    @Modifying
    @Query(value = "UPDATE  m_movie SET  " +
            "title = :#{#movie.title}, " +
            "description = :#{#movie.description}, " +
            "duration_in_minutes = :#{#movie.durationInMinutes}, " +
            "poster_image = :#{#movie.posterImage}, " +
            "release_date = :#{#movie.releaseDate} " +
            "WHERE id = :#{#movie.id}", nativeQuery = true)
    void updateMovieById(@Param("movie") Movie movie);

    @Query(value = "SELECT m.id, m.title, m.description, m.poster_image, m.release_date, m.duration_in_minutes, " +
            "mg.genre_id " +
            "FROM m_movie m " +
            "JOIN m_movie_genre mg ON m.id = mg.movie_id " +
            "WHERE m.id = :id", nativeQuery = true)
    Optional<Movie> findMovieById(@Param("id") UUID id);

    @Query(value = "SELECT m.id, m.title, m.description, m.poster_image, m.release_date, m.duration_in_minutes " +
            "FROM m_movie m where m.title LIKE CONCAT(:title, '%')",nativeQuery = true)
    List<Movie> getAllMovie(@Param("title") String title);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM m_movie WHERE id = :id", nativeQuery = true)
    void deleteMovieById(@Param("id") UUID id);

    @Query(value = "SELECT EXISTS (SELECT title from m_movie where title = :title)", nativeQuery = true)
    boolean existsMovieByName(@Param("title") String title);
}
