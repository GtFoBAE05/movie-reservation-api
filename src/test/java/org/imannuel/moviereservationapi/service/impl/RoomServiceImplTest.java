package org.imannuel.moviereservationapi.service.impl;

import org.imannuel.moviereservationapi.dto.mapper.RoomMapper;
import org.imannuel.moviereservationapi.dto.request.room.RoomRequest;
import org.imannuel.moviereservationapi.dto.response.room.RoomPageResponse;
import org.imannuel.moviereservationapi.dto.response.room.RoomResponse;
import org.imannuel.moviereservationapi.entity.Room;
import org.imannuel.moviereservationapi.repository.RoomRepository;
import org.imannuel.moviereservationapi.utils.PaginationUtil;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {
    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ValidationUtil validationUtil;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    void shouldCallInsertRoomWhenCreateRoom() {
        RoomRequest roomRequest = RoomRequest.builder()
                .name("Theater 1")
                .build();
        Mockito.doNothing().when(validationUtil).validate(roomRequest);
        Mockito.doNothing().when(roomRepository).insertRoom(Mockito.any(Room.class));

        roomService.createRoom(roomRequest);

        Mockito.verify(validationUtil, Mockito.times(1)).validate(roomRequest);
        Mockito.verify(roomRepository, Mockito.times(1)).insertRoom(Mockito.any(Room.class));  // lenient verification
    }

    @Test
    void shouldReturnRoomWhenFindRoomById() {
        Long id = 1L;
        Room expectedRoom = Room.builder()
                .id(id)
                .name("Room 1")
                .seats(List.of())
                .build();
        Mockito.when(roomRepository.findRoomById(id)).thenReturn(Optional.of(expectedRoom));

        Room room = roomService.findRoomById(id);

        assertEquals(expectedRoom.getId(), room.getId());
        assertEquals(expectedRoom.getName(), room.getName());
        assertEquals(expectedRoom.getSeats().size(), room.getSeats().size());
        Mockito.verify(roomRepository, Mockito.times(1))
                .findRoomById(id);
    }

    @Test
    void shouldThrowErrorWhenFindGenreById() {
        Long id = 99L;
        Mockito.when(roomRepository.findRoomById(id)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> roomService.findRoomById(id));

        assertEquals("Room not found", exception.getReason());
        Mockito.verify(roomRepository, Mockito.times(1))
                .findRoomById(id);
    }

    @Test
    void shouldReturnRoomResponseWhenGetRoomById() {
        Long id = 1L;
        Room room = Room.builder()
                .id(id)
                .name("Room 1")
                .seats(List.of())
                .build();
        Mockito.when(roomRepository.findRoomById(id)).thenReturn(Optional.of(room));
        RoomResponse expectedRoomResponse = RoomMapper.roomToRoomResponse(room);

        RoomResponse roomResponse = roomService.getRoomById(id);

        assertEquals(expectedRoomResponse.getId(), roomResponse.getId());
        assertEquals(expectedRoomResponse.getName(), roomResponse.getName());
        assertEquals(expectedRoomResponse.getSeats().size(), roomResponse.getSeats().size());
        Mockito.verify(roomRepository, Mockito.times(1))
                .findRoomById(id);
    }

    @Test
    void shouldReturnAllRoomWhenGetAllRoom() {
        int page = 0;
        int size = 10;
        int offset = PaginationUtil.calculateOffset(page, size);
        long totalElements = 1L;
        int totalPages = PaginationUtil.calculateTotalPages(totalElements, size);
        ;
        List<Room> roomList = List.of(Room.builder()
                .id(1L)
                .name("Room 1")
                .seats(List.of())
                .build());
        RoomPageResponse expectedRoomPageResponse = RoomPageResponse.builder()
                .rooms(RoomMapper.roomListToRoomListResponse(roomList))
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();

        Mockito.when(roomRepository.getAllRoom(size, offset)).thenReturn(roomList);
        Mockito.when(roomRepository.countTotalRooms()).thenReturn(totalElements);

        RoomPageResponse allRoom = roomService.getAllRoom(page, size);
        assertEquals(expectedRoomPageResponse.getRooms().size(), allRoom.getRooms().size());
        assertEquals(expectedRoomPageResponse.getTotalElements(), allRoom.getTotalElements());
        assertEquals(expectedRoomPageResponse.getTotalPages(), allRoom.getTotalPages());
        assertEquals(expectedRoomPageResponse.getCurrentPage(), allRoom.getCurrentPage());
        assertEquals(expectedRoomPageResponse.getPageSize(), allRoom.getPageSize());

        Mockito.verify(roomRepository, Mockito.times(1)).getAllRoom(size, offset);
        Mockito.verify(roomRepository, Mockito.times(1)).countTotalRooms();
    }

    @Test
    void shouldCallUpdateRoomWhenUpdateRoom() {
        long id = 1L;
        RoomRequest roomRequest = RoomRequest.builder()
                .name("Room 1")
                .build();
        Room room = Room.builder()
                .id(1L)
                .name(roomRequest.getName())
                .seats(List.of())
                .build();
        Mockito.when(roomRepository.findRoomById(id)).thenReturn(Optional.of(room));
        Mockito.doNothing().when(validationUtil).validate(roomRequest);
        Mockito.doNothing().when(roomRepository).updateRoom(room);

        roomService.updateRoom(id, roomRequest);

        Mockito.verify(validationUtil, Mockito.times(1)).validate(roomRequest);
        Mockito.verify(roomRepository, Mockito.times(1)).updateRoom(room);
    }

    @Test
    void shouldCallDeleteRoomWhenDeleteRoom() {
        long id = 1L;
        Room room = Room.builder()
                .id(1L)
                .name("Room 1")
                .seats(List.of())
                .build();
        Mockito.when(roomRepository.findRoomById(id)).thenReturn(Optional.of(room));
        Mockito.doNothing().when(roomRepository).deleteSeatsByRoomId(id);
        Mockito.doNothing().when(roomRepository).deleteRoom(id);

        roomService.deleteRoom(id);

        Mockito.verify(roomRepository, Mockito.times(1)).deleteSeatsByRoomId(id);
        Mockito.verify(roomRepository, Mockito.times(1)).deleteRoom(id);
    }

}