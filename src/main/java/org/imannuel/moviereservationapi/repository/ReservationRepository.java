package org.imannuel.moviereservationapi.repository;

import org.imannuel.moviereservationapi.entity.Reservation;
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
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "INSERT INTO t_reservation(id, showtime_id, user_id, is_cancel) " +
            "VALUES (:#{#reservation.id}, :#{#reservation.showtime.id}, :#{#reservation.user.id}, :#{#reservation.isCancel})", nativeQuery = true)
    void insertReservation(@Param("reservation") Reservation reservation);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "UPDATE t_reservation SET payment_id = :#{#reservation.payment.id} " +
            "WHERE id = :#{#reservation.id}", nativeQuery = true)
    void updateReservationPayment(@Param("reservation") Reservation reservation);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "INSERT INTO t_seat_reservation (reservation_id, seat_id) " +
            "VALUES (:reservationId, :seatId)", nativeQuery = true)
    void insertSeatReservation(@Param("reservationId") UUID reservationId, @Param("seatId") UUID seatId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT NOT EXISTS ( " +
            "SELECT 1 FROM t_seat_reservation sr " +
            "JOIN m_seat s ON s.id = sr.seat_id " +
            "JOIN t_showtime ts ON ts.room_id = s.room_id " +
            "WHERE s.id = :seatId " +
            "AND ts.id = :showtimeId) " +
            "OR EXISTS ( " +
            "SELECT 1 FROM t_reservation tr " +
            "JOIN t_seat_reservation tsr ON tr.id = tsr.reservation_id " +
            "JOIN t_showtime ts2 ON ts2.id = tr.showtime_id " +
            "WHERE tr.is_cancel = true " +
            "AND tsr.seat_id = :seatId " +
            "AND ts2.id = :showtimeId " +
            "AND tr.is_cancel = true)",
            nativeQuery = true)
    boolean checkIsSeatAvailable(@Param("seatId") UUID seatId, @Param("showtimeId") UUID showtimeId);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "UPDATE t_reservation " +
            "SET is_cancel = true " +
            "WHERE id = :reservationId",
            nativeQuery = true)
    void cancelReservation(@Param("reservationId") UUID reservationId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM t_reservation tr " +
            "JOIN t_showtime ts " +
            "ON ts.id = tr.showtime_id " +
            "WHERE ts.start_date_time > CURRENT_TIMESTAMP AND ts.start_date_time  > CURRENT_TIMESTAMP + INTERVAL '1' DAY  AND tr.id = :reservationId", nativeQuery = true)
    boolean checkIsReservationIsCancelable(@Param("reservationId") UUID reservationId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT id, showtime_id, user_id, is_cancel, payment_id FROM t_reservation WHERE id = :reservationId", nativeQuery = true)
    Optional<Reservation> findReservationById(@Param("reservationId") UUID reservationId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT id, showtime_id, user_id, is_cancel, payment_id FROM t_reservation WHERE user_id = :userId " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Reservation> getAllReservationByUserId(@Param("userId") UUID userId, @Param("limit") int limit, @Param("offset") int offset);

    @Transactional(readOnly = true)
    @Query(value = "SELECT COUNT(*) FROM t_reservation WHERE user_id = :userId", nativeQuery = true)
    long countTotalReservationByUserId(@Param("userId") UUID userId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT EXISTS(SELECT 1 FROM t_reservation WHERE user_id = :userId AND id = :reservationId)", nativeQuery = true)
    boolean existsByReservationIdIdAndUserAccountId(@Param("reservationId") UUID reservationId, @Param("userId") UUID userId);
}
