package org.imannuel.moviereservationapi.dto.response.payment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private String reservationId;
    private Long amount;
    private String paymentStatus;
    private String tokenSnap;
    private String redirectUrl;
}
