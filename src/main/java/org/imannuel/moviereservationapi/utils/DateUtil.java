package org.imannuel.moviereservationapi.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDate stringToLocalDate(String dateString) {
        try {
            return LocalDate.parse(dateString);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date, please use yyyy-mm-dd format");
        }
    }

    public static LocalDateTime stringToLocalDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString, formatter);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date time format. Please use yyyy-MM-dd HH:mm:ss format");
        }
    }
}
