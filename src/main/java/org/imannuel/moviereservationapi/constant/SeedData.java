package org.imannuel.moviereservationapi.constant;

import org.imannuel.moviereservationapi.dto.request.movie.MovieRequest;

import java.util.List;

public class SeedData {
    public static List<String> genreSeedData = List.of("Comedy", "Romance", "Thriller", "Action", "Adventure");

    public static List<MovieRequest> movieSeedData = List.of(
            new MovieRequest(
                    "The Avengers",
                    "Movie about a team of superheroes.",
                    143,
                    "https://cdn.vectorstock.com/i/1000v/58/48/blank-photo-icon-vector-3265848.jpg",
                    List.of(),
                    "2012-05-04"
            ),
            new MovieRequest(
                    "The Dark Knight",
                    "Movie about batman vs joker",
                    152,
                    "https://cdn.vectorstock.com/i/1000v/58/48/blank-photo-icon-vector-3265848.jpg",
                    List.of(),
                    "2008-07-18"
            ),
            new MovieRequest(
                    "Inception",
                    "Movie about inception.",
                    148,
                    "https://cdn.vectorstock.com/i/1000v/58/48/blank-photo-icon-vector-3265848.jpg",
                    List.of(),
                    "2010-07-16"
            ),
            new MovieRequest(
                    "Titanic",
                    "A best movie ever in history.",
                    195,
                    "https://cdn.vectorstock.com/i/1000v/58/48/blank-photo-icon-vector-3265848.jpg",
                    List.of(),
                    "1997-12-19"
            ),
            new MovieRequest(
                    "The Matrix",
                    "Movie about computer hacker learns save the world.",
                    136,
                    "https://cdn.vectorstock.com/i/1000v/58/48/blank-photo-icon-vector-3265848.jpg",
                    List.of(),
                    "1999-03-31"
            )
    );

    public static List<String> roomSeedData = List.of("Theater 1", "Theater 2", "Theater 3", "Theater 4", "Theater 5");

    public static List<String> seatSeedData = List.of("A1", "A2", "A3", "A4", "A5", "B1", "B2", "B3", "B4", "B5" );
}
