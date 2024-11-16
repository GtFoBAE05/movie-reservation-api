package org.imannuel.moviereservationapi.entity;

import jakarta.persistence.*;
import org.imannuel.moviereservationapi.constant.Constant;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = Constant.PAYMENT_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Payment {
    @Id
    private UUID id;

    @OneToOne
    private Reservation reservation;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @ManyToOne
    @JoinColumn(name = "payment_status_id")
    private PaymentStatus paymentStatus;

    @Column(name = "token_snap")
    private String tokenSnap;

    @Column(name = "redirect_url")
    private String redirectUrl;
}

