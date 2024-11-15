package org.imannuel.moviereservationapi.repository;

import org.imannuel.moviereservationapi.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, UUID> {
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "INSERT INTO t_showtime (id, movie_id, room_id, start_date_time, price) " +
            "VALUES (:#{#showtime.id}, :#{#showtime.movie.id}, :#{#showtime.room.id}, :#{#showtime.startDateTime}, " +
            ":#{#showtime.price})", nativeQuery = true)
    void insertShowtime(@Param("showtime") Showtime showtime);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "UPDATE t_showtime SET " +
            "movie_id = :#{#showtime.movie.id}, " +
            "room_id = :#{#showtime.room.id}, " +
            "start_date_time = :#{#showtime.startDateTime}, " +
            "price = :#{#showtime.price} " +
            "WHERE id = :#{#showtime.id}", nativeQuery = true)
    void updateShowtime(@Param("showtime") Showtime showtime);

    @Transactional(readOnly = true)
    @Query(value = "SELECT id, movie_id, room_id, start_date_time, price " +
            "FROM t_showtime " +
            "WHERE id = :id", nativeQuery = true)
    Optional<Showtime> findShowtimeById(@Param("id") UUID id);

    @Transactional(readOnly = true)
    @Query(value = "SELECT id, movie_id, room_id, start_date_time, price " +
            "FROM t_showtime " +
            "ORDER BY start_date_time", nativeQuery = true)
    List<Showtime> getAllShowtimes();

    @Transactional(readOnly = true)
    @Query(value = "SELECT id, movie_id, room_id, start_date_time, price " +
            "FROM t_showtime " +
            "WHERE t_showtime.start_date_time < CURRENT_TIMESTAMP " +
            "ORDER BY start_date_time DESC", nativeQuery = true)
    List<Showtime> findAllHistoryShowtimes();

    @Transactional(readOnly = true)
    @Query(value = "SELECT id, movie_id, room_id, start_date_time, price " +
            "FROM t_showtime " +
            "WHERE start_date_time >= CURRENT_TIMESTAMP " +
            "AND (CAST(:date AS DATE) IS NULL OR CAST(start_date_time AS DATE) = :date) " +
            "AND (:movieId IS NULL OR movie_id = :movieId) " +
            "ORDER BY start_date_time", nativeQuery = true)
    List<Showtime> findShowtimes(@Param("date") LocalDate date, @Param("movieId") UUID movieId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM t_showtime " +
            "WHERE start_date_time > CURRENT_DATE AND start_date_time < CURRENT_DATE + INTERVAL '1' DAY AND id = :id", nativeQuery = true)
    boolean isShowtimeUpdatable(@Param("id") UUID id);

    @Transactional(readOnly = true)
    @Query(value = "SELECT EXISTS (" +
            "SELECT 1 " +
            "FROM t_showtime " +
            "WHERE start_date_time > CURRENT_DATE " +
            "AND id = :showtimeId)", nativeQuery = true)
    boolean checkIsShowtimeIsReserveable(@Param("showtimeId") UUID showtimeId);
}
