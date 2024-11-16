package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.dto.request.midtrans.MidtransNotificationRequest;
import org.imannuel.moviereservationapi.dto.request.reservation.ReservationRequest;
import org.imannuel.moviereservationapi.dto.response.payment.PaymentResponse;
import org.imannuel.moviereservationapi.entity.Payment;
import org.imannuel.moviereservationapi.entity.Reservation;

public interface PaymentService {
    Payment createPayment(Reservation reservation, ReservationRequest reservationRequest);

    Payment findPaymentById(String paymentId);

    Payment findPaymentByReservationId(String paymentId);

    void updatePaymentStatus(String reservationId, String toPaymentStatus);

    void receivePaymentNotification(MidtransNotificationRequest midtransNotificationRequest);
}
