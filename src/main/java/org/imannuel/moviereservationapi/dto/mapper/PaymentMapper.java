package org.imannuel.moviereservationapi.dto.mapper;

import org.imannuel.moviereservationapi.dto.response.payment.PaymentResponse;
import org.imannuel.moviereservationapi.entity.Payment;


public class PaymentMapper {
    public static PaymentResponse paymentToPaymentResponse(Payment payment){
        return PaymentResponse.builder()
                .reservationId(payment.getReservation().getId().toString())
                .redirectUrl(payment.getRedirectUrl())
                .tokenSnap(payment.getTokenSnap())
                .paymentStatus(payment.getPaymentStatus().getName())
                .amount(payment.getAmount())
                .build();
    }
}
