package org.imannuel.moviereservationapi.repository;

import org.imannuel.moviereservationapi.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Integer> {
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "INSERT INTO m_payment_status(name) values (:name)", nativeQuery = true)
    void insertPaymentStatus(@Param("name") String name);

    @Transactional(readOnly = true)
    @Query(value = "SELECT id, name from m_payment_status where id = :id", nativeQuery = true)
    Optional<PaymentStatus> findPaymentStatusById(@Param("id") Integer id);

    @Transactional(readOnly = true)
    @Query(value = "SELECT id, name FROM m_payment_status WHERE name ILIKE :name", nativeQuery = true)
    Optional<PaymentStatus> findPaymentStatusByName(@Param("name") String name);

    @Transactional(readOnly = true)
    @Query(value = "SELECT EXISTS (SELECT name FROM m_payment_status WHERE name ILIKE :name)", nativeQuery = true)
    boolean existsPaymentStatusByName(@Param("name") String name);
}
