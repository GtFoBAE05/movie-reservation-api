package org.imannuel.moviereservationapi.utils;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {
    @Test
    void shouldReturnLocalDateWhenStringToLocalDate(){
            String stringDate = "2024-11-17";

            LocalDate localDate = DateUtil.stringToLocalDate(stringDate);

            assertEquals(2024, localDate.getYear());
            assertEquals(11, localDate.getMonthValue());
            assertEquals(17, localDate.getDayOfMonth());
    }

    @Test
    void shouldReturnErrorWHenStringToLocalDate() {
        String stringDate = "17-11-2024";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> DateUtil.stringToLocalDate(stringDate));

        assertEquals("Invalid date, please use yyyy-mm-dd format", exception.getReason());
    }

    @Test
    void shouldReturnLocalDateTimeWhenStringToLocalDateTime(){
        String stringDate = "2024-11-17 10:00:00";

        LocalDateTime localDateTime = DateUtil.stringToLocalDateTime(stringDate);

        assertEquals(2024, localDateTime.getYear());
        assertEquals(11, localDateTime.getMonthValue());
        assertEquals(17, localDateTime.getDayOfMonth());
        assertEquals(10, localDateTime.getHour());
        assertEquals(0, localDateTime.getMinute());
        assertEquals(0, localDateTime.getSecond());
    }

    @Test
    void testStringToLocalDateTimeInvalid() {
        String stringDate = "2024-11-17";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> DateUtil.stringToLocalDateTime(stringDate));
        assertEquals("Invalid date time format. Please use yyyy-MM-dd HH:mm:ss format", exception.getReason());
    }
}