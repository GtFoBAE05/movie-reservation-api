package org.imannuel.moviereservationapi.service.impl;

import org.imannuel.moviereservationapi.dto.mapper.ShowtimeMapper;
import org.imannuel.moviereservationapi.dto.request.showtime.ShowtimeRequest;
import org.imannuel.moviereservationapi.dto.response.Seat.SeatListResponse;
import org.imannuel.moviereservationapi.dto.response.showtime.ShowtimePageResponse;
import org.imannuel.moviereservationapi.dto.response.showtime.ShowtimeResponse;
import org.imannuel.moviereservationapi.entity.Movie;
import org.imannuel.moviereservationapi.entity.Room;
import org.imannuel.moviereservationapi.entity.Seat;
import org.imannuel.moviereservationapi.entity.Showtime;
import org.imannuel.moviereservationapi.repository.ShowtimeRepository;
import org.imannuel.moviereservationapi.service.MovieService;
import org.imannuel.moviereservationapi.service.RoomService;
import org.imannuel.moviereservationapi.service.SeatService;
import org.imannuel.moviereservationapi.utils.PaginationUtil;
import org.imannuel.moviereservationapi.utils.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShowtimeServiceImplTest {
    @Mock
    private ShowtimeRepository showtimeRepository;
    @Mock
    private MovieService movieService;

    @Mock
    private RoomService roomService;

    @Mock
    private SeatService seatService;

    @Mock
    private ValidationUtil validationUtil;

    @InjectMocks
    private ShowtimeServiceImpl showtimeService;

    @Test
    void shouldCallInsertShowtimeWhenCreateShowtime() {
        ShowtimeRequest showtimeRequest = ShowtimeRequest.builder()
                .startDateTime("2024-11-17 15:30:45")
                .build();
        Mockito.doNothing().when(validationUtil).validate(showtimeRequest);
        Mockito.doNothing().when(showtimeRepository).insertShowtime(Mockito.any(Showtime.class));

        showtimeService.createShowtime(showtimeRequest);

        Mockito.verify(validationUtil, Mockito.times(1)).validate(showtimeRequest);
        Mockito.verify(showtimeRepository, Mockito.times(1)).insertShowtime(Mockito.any(Showtime.class));
    }

    @Test
    void shouldCallUpdateShowtimeWhenUpdateShowtime() {
        UUID id = UUID.randomUUID();
        ShowtimeRequest showtimeRequest = ShowtimeRequest.builder()
                .startDateTime("2024-11-17 15:30:45")
                .build();
        Showtime showtime = Showtime.builder()
                .id(UUID.randomUUID())
                .room(Room.builder().build())
                .movie(Movie.builder().build())
                .price(50000L)
                .startDateTime(LocalDateTime.parse("2024-11-17T15:30:45.123456"))
                .build();

        Mockito.doNothing().when(validationUtil).validate(showtimeRequest);
        Mockito.when(showtimeRepository.findShowtimeById(id)).thenReturn(Optional.of(showtime));
        Mockito.when(showtimeRepository.isShowtimeUpdatable(id)).thenReturn(true);

        showtimeService.updateShowtime(id.toString(), showtimeRequest);

        Mockito.verify(validationUtil, Mockito.times(1)).validate(showtimeRequest);
        Mockito.verify(showtimeRepository, Mockito.times(1)).updateShowtime(Mockito.any(Showtime.class));
    }

    @Test
    void shouldReturnShowTimeResponseWhenGetShowtimeById() {
        UUID id = UUID.randomUUID();
        ShowtimeResponse showtimeResponse = ShowtimeResponse.builder()
                .id(id.toString())
                .price(50000L)
                .startDateTime("2024-11-17 15:30:45")
                .build();
        Showtime expectedShowtime = Showtime.builder()
                .id(id)
                .room(Room.builder().build())
                .movie(Movie.builder()
                        .id(UUID.randomUUID())
                        .genres(List.of())
                        .images(List.of())
                        .releaseDate(LocalDate.now())
                        .build())
                .price(50000L)
                .startDateTime(LocalDateTime.parse("2024-11-17T15:30:45"))
                .build();
        Mockito.when(showtimeRepository.findShowtimeById(id)).thenReturn(Optional.of(expectedShowtime));

        ShowtimeResponse showtime = showtimeService.getShowtimeById(id.toString());

        assertEquals(showtimeResponse.getId(), showtime.getId());
        assertEquals(showtimeResponse.getPrice(), showtime.getPrice());
        Mockito.verify(showtimeRepository, Mockito.times(1))
                .findShowtimeById(id);
    }

    @Test
    void shouldThrowErrorWhenGetShowtimeById() {
        UUID id = UUID.randomUUID();
        Mockito.when(showtimeRepository.findShowtimeById(id)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> showtimeService.findShowtimeById(id.toString()));

        assertEquals("Showtime not found", exception.getReason());
        Mockito.verify(showtimeRepository, Mockito.times(1))
                .findShowtimeById(id);
    }

    @Test
    void shouldReturnAllShowtimeWhenGetAllShowtime() {
        int page = 0;
        int size = 10;
        int offset = PaginationUtil.calculateOffset(page, size);
        long totalElements = 1L;
        int totalPages = PaginationUtil.calculateTotalPages(totalElements, size);

        List<Showtime> showtimeList = List.of(Showtime.builder()
                .id(UUID.randomUUID())
                .room(Room.builder().build())
                .movie(Movie.builder()
                        .id(UUID.randomUUID())
                        .genres(List.of())
                        .images(List.of())
                        .releaseDate(LocalDate.now())
                        .build())
                .price(50000L)
                .startDateTime(LocalDateTime.parse("2024-11-17T15:30:45"))
                .build());
        ShowtimePageResponse expectedShowtimePageResponse = ShowtimePageResponse.builder()
                .showtimes(ShowtimeMapper.showtimeListToShowTimeListResponse(showtimeList))
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();

        Mockito.when(showtimeRepository.getAllShowtimes(size, offset)).thenReturn(showtimeList);
        Mockito.when(showtimeRepository.countTotalShowtime()).thenReturn(totalElements);

        ShowtimePageResponse showtimePageResponse = showtimeService.getAllShowtime(page, size);
        assertEquals(expectedShowtimePageResponse.getShowtimes().size(), showtimePageResponse.getShowtimes().size());
        assertEquals(expectedShowtimePageResponse.getTotalElements(), showtimePageResponse.getTotalElements());
        assertEquals(expectedShowtimePageResponse.getTotalPages(), showtimePageResponse.getTotalPages());
        assertEquals(expectedShowtimePageResponse.getCurrentPage(), showtimePageResponse.getCurrentPage());
        assertEquals(expectedShowtimePageResponse.getPageSize(), showtimePageResponse.getPageSize());

        Mockito.verify(showtimeRepository, Mockito.times(1)).getAllShowtimes(size, offset);
        Mockito.verify(showtimeRepository, Mockito.times(1)).countTotalShowtime();
    }

    @Test
    void shouldReturnFilteredShowtimeWhenGetFilteredShowtime() {
        int page = 0;
        int size = 10;
        int offset = PaginationUtil.calculateOffset(page, size);
        long totalElements = 1L;
        int totalPages = PaginationUtil.calculateTotalPages(totalElements, size);

        List<Showtime> showtimeList = List.of(Showtime.builder()
                .id(UUID.randomUUID())
                .room(Room.builder().build())
                .movie(Movie.builder()
                        .id(UUID.randomUUID())
                        .genres(List.of())
                        .images(List.of())
                        .releaseDate(LocalDate.now())
                        .build())
                .price(50000L)
                .startDateTime(LocalDateTime.parse("2024-11-17T15:30:45"))
                .build());
        ShowtimePageResponse expectedShowtimePageResponse = ShowtimePageResponse.builder()
                .showtimes(ShowtimeMapper.showtimeListToShowTimeListResponse(showtimeList))
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();

        Mockito.when(showtimeRepository.findShowtimes(null, null, size, offset)).thenReturn(showtimeList);
        Mockito.when(showtimeRepository.countTotalFilteredShowtimes(null, null)).thenReturn(totalElements);

        ShowtimePageResponse showtimePageResponse = showtimeService.getShowtimeBy(null, null, page, size);
        assertEquals(expectedShowtimePageResponse.getShowtimes().size(), showtimePageResponse.getShowtimes().size());
        assertEquals(expectedShowtimePageResponse.getTotalElements(), showtimePageResponse.getTotalElements());
        assertEquals(expectedShowtimePageResponse.getTotalPages(), showtimePageResponse.getTotalPages());
        assertEquals(expectedShowtimePageResponse.getCurrentPage(), showtimePageResponse.getCurrentPage());
        assertEquals(expectedShowtimePageResponse.getPageSize(), showtimePageResponse.getPageSize());

        Mockito.verify(showtimeRepository, Mockito.times(1)).findShowtimes(null, null, size, offset);
        Mockito.verify(showtimeRepository, Mockito.times(1)).countTotalFilteredShowtimes(null, null);
    }

    @Test
    void shouldReturnVoidWhenCheckIsShowtimeUpdatable() {
        UUID id = UUID.randomUUID();

        Mockito.when(showtimeRepository.isShowtimeUpdatable(id)).thenReturn(true);

        showtimeService.checkIsShowtimeUpdateable(id.toString());
        Mockito.verify(showtimeRepository, Mockito.times(1)).isShowtimeUpdatable(id);
    }

    @Test
    void shouldThrowErrorWhenCheckIsShowtimeUpdatable() {
        UUID id = UUID.randomUUID();
        Mockito.when(showtimeRepository.isShowtimeUpdatable(id)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> showtimeService.checkIsShowtimeUpdateable(id.toString()));

        assertEquals("Showtime is not updatable", exception.getReason());
    }

    @Test
    void shouldReturnSeatListResponseWhenGetAvailableSeat() {
        Showtime showtime = Showtime.builder()
                .id(UUID.randomUUID())
                .room(Room.builder().build())
                .movie(Movie.builder()
                        .id(UUID.randomUUID())
                        .genres(List.of())
                        .images(List.of())
                        .releaseDate(LocalDate.now())
                        .build())
                .price(50000L)
                .startDateTime(LocalDateTime.parse("2024-11-17T15:30:45"))
                .build();
        List<Seat> seatList = List.of(
                Seat.builder()
                        .id(UUID.randomUUID())
                        .seatCode("A1")
                        .build()
        );

        Mockito.when(showtimeRepository.findShowtimeById(showtime.getId())).thenReturn(Optional.of(showtime));
        Mockito.when(seatService.getAvailableSeatForShowtime(showtime.getId().toString())).thenReturn(seatList);

        SeatListResponse availableSeat = showtimeService.getAvailableSeat(showtime.getId().toString());

        assertEquals(seatList.size(), availableSeat.getSeats().size());
        Mockito.verify(seatService, Mockito.times(1)).getAvailableSeatForShowtime(showtime.getId().toString());
    }

    @Test
    void shouldReturnTrueWhenCheckIsShowtimeIsReserveable() {
        Showtime showtime = Showtime.builder()
                .id(UUID.randomUUID())
                .room(Room.builder().build())
                .movie(Movie.builder()
                        .id(UUID.randomUUID())
                        .genres(List.of())
                        .images(List.of())
                        .releaseDate(LocalDate.now())
                        .build())
                .price(50000L)
                .startDateTime(LocalDateTime.parse("2024-11-17T15:30:45"))
                .build();
        Mockito.when(showtimeRepository.findShowtimeById(showtime.getId())).thenReturn(Optional.of(showtime));
        Mockito.when(showtimeRepository.checkIsShowtimeIsReserveable(showtime.getId())).thenReturn(true);

        boolean b = showtimeService.checkIsShowtimeIsReserveable(showtime.getId().toString());
        assertTrue(b);
        Mockito.verify(showtimeRepository, Mockito.times(1)).findShowtimeById(showtime.getId());
        Mockito.verify(showtimeRepository, Mockito.times(1)).checkIsShowtimeIsReserveable(showtime.getId());
    }
}