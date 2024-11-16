package org.imannuel.moviereservationapi.service;


import org.imannuel.moviereservationapi.entity.PaymentStatus;

public interface PaymentStatusService {
    void createPaymentStatus(String name);

    PaymentStatus findPaymentStatusById(Integer id);

    PaymentStatus findPaymentStatusByName(String name);

    boolean checkIsPaymentStatusExists(String name);
}
