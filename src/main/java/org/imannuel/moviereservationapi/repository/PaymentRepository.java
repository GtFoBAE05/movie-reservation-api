package org.imannuel.moviereservationapi.repository;

import org.imannuel.moviereservationapi.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "INSERT INTO t_payment(id, amount, redirect_url, token_snap, payment_status_id, reservation_id) " +
            "VALUES(:#{#payment.id}, :#{#payment.amount}, :#{#payment.redirectUrl}, " +
            ":#{#payment.tokenSnap}, :#{#payment.paymentStatus.id}, :#{#payment.reservation.id})", nativeQuery = true)
    void insertPayment(@Param("payment") Payment payment);

    @Transactional
    @Modifying
    @Query(value = "UPDATE t_payment SET payment_status_id = :#{#payment.paymentStatus.id} " +
            "WHERE id = :#{#payment.id}", nativeQuery = true)
    void updatePayment(@Param("payment") Payment payment);

    @Transactional(readOnly = true)
    @Query(value = "SELECT id, amount, redirect_url, token_snap, payment_status_id, reservation_id " +
            "FROM t_payment WHERE id = :id", nativeQuery = true)
    Optional<Payment> findPaymentById(@Param("id") UUID id);

    @Transactional(readOnly = true)
    @Query(value = "SELECT id, amount, redirect_url, token_snap, payment_status_id, reservation_id " +
            "FROM t_payment WHERE reservation_id = :id", nativeQuery = true)
    Optional<Payment> findPaymentByReservationId(@Param("id") UUID id);
}
