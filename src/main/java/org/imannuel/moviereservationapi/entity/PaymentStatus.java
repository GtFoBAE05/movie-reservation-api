package org.imannuel.moviereservationapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.imannuel.moviereservationapi.constant.Constant;

@Entity
@Table(name = Constant.PAYMENT_STATUS_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
}
