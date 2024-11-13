package org.imannuel.moviereservationapi.repository;

import org.imannuel.moviereservationapi.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO m_genre(name) values (:name)", nativeQuery = true)
    void insertGenre(@Param("name") String name);

    @Query(value = "SELECT id, name from m_genre where id = :id", nativeQuery = true)
    Optional<Genre> findGenreById(@Param("id") Long id);

    @Query(value = "SELECT id, name from m_genre", nativeQuery = true)
    List<Genre> getAllGenre();

    @Transactional
    @Modifying
    @Query(value = "UPDATE m_genre set name = :name where id = :id", nativeQuery = true)
    void updateGenreById(@Param("id") Long id, @Param("name") String genreNameTo);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM m_genre where id = :id", nativeQuery = true)
    void deleteGenreById(@Param("id") Long id);

    @Query(value = "SELECT EXISTS (SELECT name FROM m_genre WHERE name ILIKE :name)", nativeQuery = true)
    boolean existsGenreByName(@Param("name") String name);


}
