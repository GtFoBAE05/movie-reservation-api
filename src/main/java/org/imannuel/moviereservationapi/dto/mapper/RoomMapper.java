package org.imannuel.moviereservationapi.dto.mapper;

import org.imannuel.moviereservationapi.dto.request.room.RoomRequest;
import org.imannuel.moviereservationapi.dto.response.room.RoomListResponse;
import org.imannuel.moviereservationapi.dto.response.room.RoomResponse;
import org.imannuel.moviereservationapi.dto.response.room.RoomWithoutSeatResponse;
import org.imannuel.moviereservationapi.entity.Room;

import java.util.List;

public class RoomMapper {
    public static Room roomRequestToRoom(RoomRequest roomRequest) {
        return Room.builder()
                .name(roomRequest.getName())
                .build();
    }

    public static RoomResponse roomToRoomResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .seats(room.getSeats().stream().map(seat -> SeatMapper.seatToSeatResponse(seat)).toList())
                .build();
    }

    public static RoomWithoutSeatResponse roomToRoomWithoutSeatResponse(Room room) {
        return RoomWithoutSeatResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .build();
    }

    public static RoomListResponse roomListToRoomListResponse(List<Room> rooms) {
        return RoomListResponse.builder()
                .rooms(rooms.stream().map(
                        room -> roomToRoomResponse(room)
                ).toList())
                .build();
    }


}

