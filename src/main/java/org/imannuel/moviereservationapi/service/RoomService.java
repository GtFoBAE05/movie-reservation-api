package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.dto.request.room.RoomRequest;
import org.imannuel.moviereservationapi.dto.response.room.RoomListResponse;
import org.imannuel.moviereservationapi.dto.response.room.RoomResponse;
import org.imannuel.moviereservationapi.entity.Room;

public interface RoomService {
    void createRoom(RoomRequest roomRequest);

    Room findRoomById(Long id);

    Room findRoomByName(String name);

    RoomResponse getRoomById(Long id);

    RoomListResponse getAllRoom();

    void updateRoom(Long id, RoomRequest roomRequest);

    void deleteRoom(Long id);

    boolean checkIsRoomExists(String name);
}
