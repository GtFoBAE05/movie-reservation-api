package org.imannuel.moviereservationapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.imannuel.moviereservationapi.constant.Constant;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = Constant.RESERVATION_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Reservation {
    @Id
    private UUID id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = Constant.SEAT_RESERVATION_TABLE,
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "seat_id")
    )
    private List<Seat> seats;

    @Column(name = "is_cancel", nullable = false)
    private Boolean isCancel;
}
