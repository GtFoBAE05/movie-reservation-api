package org.imannuel.moviereservationapi.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

public class DateParse {
    public static LocalDate stringToLocalDate(String dateString) {
        try {
            return LocalDate.parse(dateString);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date, please use yyyy-mm-dd format");
        }
    }
}
