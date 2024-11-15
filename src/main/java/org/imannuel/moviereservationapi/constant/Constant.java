package org.imannuel.moviereservationapi.constant;

import java.util.List;

public class Constant {
    public static final String FILE_TABLE = "m_file";
    public static final String ROLE_TABLE = "m_role";
    public static final String USER_TABLE = "m_user";
    public static final String GENRE_TABLE = "m_genre";
    public static final String MOVIE_TABLE = "m_movie";
    public static final String MOVIE_IMAGE_TABLE = "m_movie_image";
    public static final String MOVIE_GENRE_TABLE = "m_movie_genre";
    public static final String ROOM_TABLE = "m_room";
    public static final String SEAT_TABLE = "m_seat";
    public static final String SHOWTIME_TABLE = "t_showtime";
    public static final String RESERVATION_TABLE = "t_reservation";
    public static final String SEAT_RESERVATION_TABLE = "t_seat_reservation";
    public static final String AUTH_API = "/api/auth";
    public static final String USER_API = "/api/users";
    public static List<String> contentTypes = List.of("image/jpg", "image/png", "image/webp", "image/jpeg");
}
