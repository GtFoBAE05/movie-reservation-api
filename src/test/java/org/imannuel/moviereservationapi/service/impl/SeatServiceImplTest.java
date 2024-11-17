package org.imannuel.moviereservationapi.service.impl;

import org.imannuel.moviereservationapi.dto.request.seat.SeatRequest;
import org.imannuel.moviereservationapi.dto.response.Seat.SeatListResponse;
import org.imannuel.moviereservationapi.entity.Room;
import org.imannuel.moviereservationapi.entity.Seat;
import org.imannuel.moviereservationapi.repository.SeatRepository;
import org.imannuel.moviereservationapi.service.RoomService;
import org.imannuel.moviereservationapi.utils.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class SeatServiceImplTest {
    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private SeatServiceImpl seatService;

    @Test
    void shouldCallInsertSeatWhenCreateSeat() {
        Room room = Room.builder()
                .id(1L)
                .name("Room 1")
                .seats(List.of())
                .build();
        SeatRequest seatRequest = SeatRequest.builder()
                .seatCode("A1")
                .roomId(1L)
                .build();
        Mockito.doNothing().when(validationUtil).validate(seatRequest);
        Mockito.when(roomService.findRoomById(room.getId())).thenReturn(room);
        Mockito.doNothing().when(seatRepository).insertSeat(Mockito.any(Seat.class));

        seatService.createSeat(seatRequest);

        Mockito.verify(validationUtil, Mockito.times(1)).validate(seatRequest);
        Mockito.verify(seatRepository, Mockito.times(1)).insertSeat(Mockito.any(Seat.class));  // lenient verification
    }

    @Test
    void shouldReturnSeatWhenFindSeatById() {
        Room room = Room.builder()
                .id(1L)
                .name("Room 1")
                .seats(List.of())
                .build();
        UUID id = UUID.randomUUID();
        Seat expectedSeat = Seat.builder()
                .id(id)
                .seatCode("A1")
                .room(room)
                .build();
        Mockito.when(seatRepository.findSeatById(id)).thenReturn(Optional.of(expectedSeat));

        Seat seat = seatService.findSeatById(id.toString());

        assertEquals(expectedSeat.getId(), seat.getId());
        assertEquals(expectedSeat.getSeatCode(), seat.getSeatCode());
        Mockito.verify(seatRepository, Mockito.times(1))
                .findSeatById(id);
    }

    @Test
    void shouldThrowErrorWhenFindSeatById() {
        UUID id = UUID.randomUUID();
        Mockito.when(seatRepository.findSeatById(id)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> seatService.findSeatById(id.toString()));

        assertEquals("Seat not found", exception.getReason());
        Mockito.verify(seatRepository, Mockito.times(1))
                .findSeatById(id);
    }

    @Test
    void shouldReturnSeatWhenFindSeatByRoomId() {
        Room room = Room.builder()
                .id(1L)
                .name("Room 1")
                .seats(List.of())
                .build();
        UUID id = UUID.randomUUID();
        List<Seat> expectedSeat = List.of(
                Seat.builder()
                        .id(id)
                        .seatCode("A1")
                        .room(room)
                        .build()
        );
        Mockito.when(seatRepository.findSeatByRoomId(room.getId())).thenReturn(expectedSeat);

        SeatListResponse seatListResponse = seatService.findSeatByRoomId(room.getId());

        assertEquals(expectedSeat.size(), seatListResponse.getSeats().size());
        Mockito.verify(seatRepository, Mockito.times(1))
                .findSeatByRoomId(room.getId());
    }

    @Test
    void shouldCallUpdateSeatWhenUpdateSeat() {
        Room room = Room.builder()
                .id(1L)
                .name("Room 1")
                .seats(List.of())
                .build();
        UUID seatId = UUID.randomUUID();
        SeatRequest seatRequest = SeatRequest.builder()
                .seatCode("A9")
                .roomId(room.getId())
                .build();
        Seat seat = Seat.builder()
                .id(seatId)
                .seatCode("A1")
                .room(room)
                .build();
        Mockito.when(seatRepository.findSeatById(seatId)).thenReturn(Optional.of(seat));
        Mockito.doNothing().when(validationUtil).validate(seatRequest);
        Mockito.doNothing().when(seatRepository).updateSeat(seat);

        seatService.updateSeat(seatId.toString(), seatRequest);

        Mockito.verify(validationUtil, Mockito.times(1)).validate(seatRequest);
        Mockito.verify(seatRepository, Mockito.times(1)).updateSeat(seat);
    }

    @Test
    void shouldCallUpdateSeatWhenUpdateSeatDifferentRoom() {
        Room room = Room.builder()
                .id(1L)
                .name("Room 1")
                .seats(List.of())
                .build();
        Room room2 = Room.builder()
                .id(2L)
                .name("Room 2")
                .seats(List.of())
                .build();
        UUID seatId = UUID.randomUUID();
        SeatRequest seatRequest = SeatRequest.builder()
                .seatCode("A9")
                .roomId(2L)
                .build();
        Seat seat = Seat.builder()
                .id(seatId)
                .seatCode("A1")
                .room(room)
                .build();
        Mockito.when(seatRepository.findSeatById(seatId)).thenReturn(Optional.of(seat));
        Mockito.when(roomService.findRoomById(room2.getId())).thenReturn(room2);

        Mockito.doNothing().when(validationUtil).validate(seatRequest);
        Mockito.doNothing().when(seatRepository).updateSeat(seat);

        seatService.updateSeat(seatId.toString(), seatRequest);

        Mockito.verify(validationUtil, Mockito.times(1)).validate(seatRequest);
        Mockito.verify(seatRepository, Mockito.times(1)).updateSeat(seat);
    }

    @Test
    void shouldCallDeleteSeatWhenDeleteSeat() {
        Room room = Room.builder()
                .id(1L)
                .name("Room 1")
                .seats(List.of())
                .build();
        UUID seatId = UUID.randomUUID();
        Seat seat = Seat.builder()
                .id(seatId)
                .seatCode("A1")
                .room(room)
                .build();
        Mockito.when(seatRepository.findSeatById(seatId)).thenReturn(Optional.of(seat));
        Mockito.doNothing().when(seatRepository).deleteSeat(seatId);

        seatService.deleteSeat(seatId.toString());

        Mockito.verify(seatRepository, Mockito.times(1)).deleteSeat(seatId);
    }

    @Test
    void shouldReturnListSeatWhenGetAvailableSeatForShowtime() {
        UUID showtimeId = UUID.randomUUID();
        Room room = Room.builder()
                .id(1L)
                .name("Room 1")
                .seats(List.of())
                .build();
        List<Seat> expectedSeat = List.of(
                Seat.builder()
                        .id(UUID.randomUUID())
                        .seatCode("A1")
                        .room(room)
                        .build()
        );
        Mockito.when(seatRepository.getAvailableSeat(showtimeId)).thenReturn(expectedSeat);

        List<Seat> availableSeatForShowtime = seatService.getAvailableSeatForShowtime(showtimeId.toString());

        assertEquals(expectedSeat.size(), availableSeatForShowtime.size());
        Mockito.verify(seatRepository, Mockito.times(1)).getAvailableSeat(showtimeId);
    }

}