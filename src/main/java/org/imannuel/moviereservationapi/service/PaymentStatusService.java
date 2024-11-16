package org.imannuel.moviereservationapi.service;


import org.imannuel.moviereservationapi.entity.PaymentStatus;

public interface PaymentStatusService {
    void createPaymentStatus(String name);

    PaymentStatus findPaymentStatusByName(String name);

    boolean checkIsPaymentStatusExists(String name);
}
