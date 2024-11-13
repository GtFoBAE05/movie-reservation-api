package org.imannuel.moviereservationapi.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.constant.SeedData;
import org.imannuel.moviereservationapi.dto.mapper.RoomMapper;
import org.imannuel.moviereservationapi.dto.request.room.RoomRequest;
import org.imannuel.moviereservationapi.dto.response.room.RoomListResponse;
import org.imannuel.moviereservationapi.dto.response.room.RoomResponse;
import org.imannuel.moviereservationapi.entity.Room;
import org.imannuel.moviereservationapi.repository.RoomRepository;
import org.imannuel.moviereservationapi.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    public void init() {
        SeedData.roomSeedData.stream().filter(
                s -> !checkIsRoomExists(s)
        ).forEach(s -> createRoom(new RoomRequest(s)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRoom(RoomRequest roomRequest) {
        Room room = RoomMapper.roomRequestToRoom(roomRequest);
        roomRepository.insertRoom(room);
    }

    @Override
    public Room findRoomById(Long id) {
        return roomRepository.findRoomById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
    }

    @Override
    public Room findRoomByName(String name) {
        return roomRepository.findRoomByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
    }

    @Override
    public RoomResponse getRoomById(Long id) {
        Room room = findRoomById(id);
        return RoomMapper.roomToRoomResponse(room);
    }

    @Override
    public RoomListResponse getAllRoom() {
        List<Room> rooms = roomRepository.getAllRoom();
        return RoomMapper.roomListToRoomListResponse(rooms);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoom(Long id, RoomRequest roomRequest) {
        Room room = findRoomById(id);
        room.setName(roomRequest.getName());
        roomRepository.updateRoom(room);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoom(Long id) {
        roomRepository.findRoomById(id);
        roomRepository.deleteSeatsByRoomId(id);
        roomRepository.deleteRoom(id);
    }

    @Override
    public boolean checkIsRoomExists(String name) {
        return roomRepository.existsRoomByName(name);
    }
}
