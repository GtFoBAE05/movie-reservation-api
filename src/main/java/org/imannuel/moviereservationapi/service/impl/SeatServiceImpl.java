package org.imannuel.moviereservationapi.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.constant.SeedData;
import org.imannuel.moviereservationapi.dto.mapper.SeatMapper;
import org.imannuel.moviereservationapi.dto.request.seat.SeatRequest;
import org.imannuel.moviereservationapi.dto.response.Seat.SeatListResponse;
import org.imannuel.moviereservationapi.entity.Room;
import org.imannuel.moviereservationapi.entity.Seat;
import org.imannuel.moviereservationapi.repository.SeatRepository;
import org.imannuel.moviereservationapi.service.RoomService;
import org.imannuel.moviereservationapi.service.SeatService;
import org.imannuel.moviereservationapi.utils.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final RoomService roomService;
    private final ValidationUtil validationUtil;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    public void init() {
        List<Room> rooms = SeedData.roomSeedData.stream()
                .map(roomService::findRoomByName)
                .filter(Objects::nonNull)
                .toList();

        rooms.forEach(room -> SeedData.seatSeedData.stream()
                .filter(seatCode -> !checkIsSeatExists(seatCode, room.getId()))
                .map(seatCode -> new SeatRequest(seatCode, room.getId()))
                .forEach(this::createSeat)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSeat(SeatRequest seatRequest) {
        validationUtil.validate(seatRequest);

        Room room = roomService.findRoomById(seatRequest.getRoomId());
        Seat seat = Seat.builder()
                .id(UUID.randomUUID())
                .seatCode(seatRequest.getSeatCode())
                .room(room)
                .build();
        seatRepository.insertSeat(seat);
    }

    @Override
    @Transactional(readOnly = true)
    public Seat findSeatById(String id) {
        return seatRepository.findSeatById(UUID.fromString(id)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public SeatListResponse findSeatByRoomId(Long id) {
        List<Seat> seats = seatRepository.findSeatByRoomId(id);
        return SeatMapper.seatListToSeatListResponse(seats);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSeat(String id, SeatRequest seatRequest) {
        validationUtil.validate(seatRequest);

        Seat seat = findSeatById(id);
        seat.setSeatCode(seatRequest.getSeatCode());
        if (!Objects.equals(seat.getRoom().getId(), seatRequest.getRoomId())) {
            Room room = roomService.findRoomById(seatRequest.getRoomId());
            seat.setRoom(room);
        }
        seatRepository.updateSeat(seat);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSeat(String id) {
        seatRepository.deleteSeat(UUID.fromString(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seat> getAvailableSeatForShowtime(String showtimeId) {
        return seatRepository.getAvailableSeat(UUID.fromString(showtimeId));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkIsSeatExists(String seatCode, Long roomId) {
        return seatRepository.seatExistsBySeatCode(seatCode, roomId);
    }
}
