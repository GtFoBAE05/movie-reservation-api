package org.imannuel.moviereservationapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.imannuel.moviereservationapi.constant.Constant;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = Constant.MOVIE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Movie {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "duration_in_minutes", nullable = false)
    private Integer durationInMinutes;

    @Column(name = "poster_image", nullable = false)
    private String posterImage;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = Constant.MOVIE_GENRE_TABLE,
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;
}
