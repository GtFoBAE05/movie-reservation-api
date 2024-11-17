package org.imannuel.moviereservationapi.repository;

import org.imannuel.moviereservationapi.entity.Seat;
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
public interface SeatRepository extends JpaRepository<Seat, UUID> {
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "INSERT INTO m_seat(id, seat_code, room_id) VALUES (:#{#seat.id},:#{#seat.seatCode},:#{#seat.room.id})", nativeQuery = true)
    void insertSeat(@Param("seat") Seat seat);

    @Transactional(readOnly = true)
    @Query(value = "SELECT id, seat_code, room_id from m_seat where id = :id", nativeQuery = true)
    Optional<Seat> findSeatById(@Param("id") UUID id);

    @Transactional(readOnly = true)
    @Query(value = "SELECT id, seat_code, room_id from m_seat where room_id = :id", nativeQuery = true)
    List<Seat> findSeatByRoomId(@Param("id") Long id);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "UPDATE m_seat SET seat_code = :#{#seat.seatCode}, room_id = :#{#seat.room.id} WHERE id = :#{#seat.id}", nativeQuery = true)
    void updateSeat(@Param("seat") Seat seat);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "DELETE FROM m_seat WHERE id = :id ", nativeQuery = true)
    void deleteSeat(@Param("id") UUID id);

    @Transactional(readOnly = true)
    @Query(value = "SELECT EXISTS(SELECT seat_code FROM m_seat WHERE seat_code = :seatCode AND room_id = :roomId)", nativeQuery = true)
    boolean seatExistsBySeatCode(@Param("seatCode") String seatCode, @Param("roomId") Long roomId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT s.id, s.seat_code, s.room_id FROM m_seat s " +
            "JOIN t_showtime ts ON ts.room_id = s.room_id " +
            "JOIN m_room mr on s.room_id = mr.id " +
            "WHERE ts.id = :showtimeId " +
            "AND NOT EXISTS (" +
            "SELECT 1 FROM t_seat_reservation sr " +
            "WHERE sr.seat_id = s.id)",
            nativeQuery = true)
    List<Seat> getAvailableSeat(@Param("showtimeId") UUID showtimeId);
}
