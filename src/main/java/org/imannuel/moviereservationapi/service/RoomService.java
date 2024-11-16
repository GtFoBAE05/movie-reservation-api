package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.dto.request.room.RoomRequest;
import org.imannuel.moviereservationapi.dto.response.room.RoomPageResponse;
import org.imannuel.moviereservationapi.dto.response.room.RoomResponse;
import org.imannuel.moviereservationapi.entity.Room;

public interface RoomService {
    void createRoom(RoomRequest roomRequest);

    Room findRoomById(Long id);

    Room findRoomByName(String name);

    RoomResponse getRoomById(Long id);

    RoomPageResponse getAllRoom(Integer page, Integer size);

    void updateRoom(Long id, RoomRequest roomRequest);

    void deleteRoom(Long id);

    boolean checkIsRoomExists(String name);
}
