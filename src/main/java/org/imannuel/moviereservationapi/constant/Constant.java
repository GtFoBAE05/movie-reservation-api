package org.imannuel.moviereservationapi.constant;

import java.util.List;

public class Constant {
    //table
    public static final String FILE_TABLE = "m_file";
    public static final String ROLE_TABLE = "m_role";
    public static final String PAYMENT_STATUS_TABLE = "m_payment_status";
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
    public static final String PAYMENT_TABLE = "t_payment";

    //api
    public static final String AUTH_API = "/api/auth";
    public static final String USER_API = "/api/users";
    public static final String FILES_API = "/api/files";
    public static final String GENRES_API = "/api/genres";
    public static final String MOVIES_API = "/api/movies";
    public static final String PAYMENTS_API = "/api/payments";
    public static final String RESERVATIONS_API = "/api/reservations";
    public static final String ROOMS_API = "/api/rooms";
    public static final String SEATS_API = "/api/seats";
    public static final String SHOWTIMES_API = "/api/showtimes";
    public static final String USERS_API = "/api/users";

    //
    public static List<String> contentTypes = List.of("image/jpg", "image/png", "image/webp", "image/jpeg");
    public static List<String> enabledPayments = List.of("bca_va", "gopay", "shopeepay", "other_qris");
}
