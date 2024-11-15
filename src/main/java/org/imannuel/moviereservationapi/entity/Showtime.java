package org.imannuel.moviereservationapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.imannuel.moviereservationapi.constant.Constant;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = Constant.SHOWTIME_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Showtime {
    @Id
    private UUID id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private Long price;
}
