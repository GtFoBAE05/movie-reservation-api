package org.imannuel.moviereservationapi.controller;

import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.room.RoomRequest;
import org.imannuel.moviereservationapi.dto.response.room.RoomListResponse;
import org.imannuel.moviereservationapi.dto.response.room.RoomResponse;
import org.imannuel.moviereservationapi.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createRoom(
            @RequestBody RoomRequest roomRequest
    ) {
        roomService.createRoom(roomRequest);
        return ApiMapper.basicMapper(HttpStatus.CREATED, "Success create room", null);
    }

    @GetMapping
    public ResponseEntity getAllRoom() {
        RoomListResponse rooms = roomService.getAllRoom();
        return ApiMapper.basicMapper(HttpStatus.OK, "Success fetch all room", rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity getRoomById(
            @PathVariable("id") Long id
    ) {
        RoomResponse room = roomService.getRoomById(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success get room", room);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateRoom(
            @PathVariable("id") Long id,
            @RequestBody RoomRequest roomRequest
    ) {
        roomService.updateRoom(id, roomRequest);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success update room", null);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteRoom(
            @PathVariable("id") Long id
    ) {
        roomService.deleteRoom(id);
        return ApiMapper.basicMapper(HttpStatus.OK, "Success delete  room", null);
    }

}
