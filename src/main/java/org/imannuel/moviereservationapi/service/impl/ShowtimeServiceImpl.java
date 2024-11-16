package org.imannuel.moviereservationapi.service.impl;

import ch.qos.logback.core.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.mapper.SeatMapper;
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
import org.imannuel.moviereservationapi.service.ShowtimeService;
import org.imannuel.moviereservationapi.utils.DateUtil;
import org.imannuel.moviereservationapi.utils.PaginationUtil;
import org.imannuel.moviereservationapi.utils.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieService movieService;
    private final RoomService roomService;
    private final SeatService seatService;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createShowtime(ShowtimeRequest showtimeRequest) {
        validationUtil.validate(showtimeRequest);

        Showtime showtime = buildShowtime(showtimeRequest);
        showtime.setId(UUID.randomUUID());
        showtimeRepository.insertShowtime(showtime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShowtime(String id, ShowtimeRequest showtimeRequest) {
        validationUtil.validate(showtimeRequest);

        findShowtimeById(id);
        checkIsShowtimeUpdateable(id);
        Showtime showtime = buildShowtime(showtimeRequest);
        showtime.setId(UUID.fromString(id));
        showtimeRepository.updateShowtime(showtime);
    }

    @Override
    @Transactional(readOnly = true)
    public Showtime findShowtimeById(String id) {
        return showtimeRepository.findShowtimeById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Showtime not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public ShowtimeResponse getShowtimeById(String id) {
        Showtime showtime = findShowtimeById(id);
        return ShowtimeMapper.showtimeToShowtimeResponse(showtime);
    }

    @Override
    @Transactional(readOnly = true)
    public ShowtimePageResponse getAllShowtime(Integer page, Integer size) {
        int offset = PaginationUtil.calculateOffset(page, size);
        long totalShowtimes = showtimeRepository.countTotalShowtime();
        int totalPages = PaginationUtil.calculateTotalPages(totalShowtimes, size);

        List<Showtime> showtimes = showtimeRepository.getAllShowtimes(size, offset);

        return ShowtimePageResponse.builder()
                .showtimes(ShowtimeMapper.showtimeListToShowTimeListResponse(showtimes))
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalShowtimes)
                .totalPages(totalPages)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ShowtimePageResponse getAllHistoryShowtime(Integer page, Integer size) {
        int offset = PaginationUtil.calculateOffset(page, size);
        long totalShowtimes = showtimeRepository.countTotalHistoryShowtimes();
        int totalPages = PaginationUtil.calculateTotalPages(totalShowtimes, size);

        List<Showtime> showtimes = showtimeRepository.findAllHistoryShowtimes(size, offset);

        return ShowtimePageResponse.builder()
                .showtimes(ShowtimeMapper.showtimeListToShowTimeListResponse(showtimes))
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalShowtimes)
                .totalPages(totalPages)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ShowtimePageResponse getShowtimeBy(String date, String movieId, Integer page, Integer size) {
        int offset = PaginationUtil.calculateOffset(page, size);
        long totalShowtimes = showtimeRepository.countTotalFilteredShowtimes(
                StringUtil.isNullOrEmpty(date) ? null : DateUtil.stringToLocalDate(date),
                StringUtil.isNullOrEmpty(movieId) ? null : UUID.fromString(movieId)
        );
        int totalPages = PaginationUtil.calculateTotalPages(totalShowtimes, size);

        List<Showtime> showtimes = showtimeRepository.findShowtimes(
                StringUtil.isNullOrEmpty(date) ? null : DateUtil.stringToLocalDate(date),
                StringUtil.isNullOrEmpty(movieId) ? null : UUID.fromString(movieId),
                size, offset
        );

        return ShowtimePageResponse.builder()
                .showtimes(ShowtimeMapper.showtimeListToShowTimeListResponse(showtimes))
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalShowtimes)
                .totalPages(totalPages)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkIsShowtimeUpdateable(String id) {
        if (!showtimeRepository.isShowtimeUpdatable(UUID.fromString(id))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Showtime is not updatable");
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public SeatListResponse getAvailableSeat(String showtimeId) {
        findShowtimeById(showtimeId);
        List<Seat> availableSeat = seatService.getAvailableSeatForShowtime(showtimeId);
        return SeatMapper.seatListToSeatListResponse(availableSeat);
    }

    private Showtime buildShowtime(ShowtimeRequest showtimeRequest) {
        Movie movie = movieService.findMovieById(showtimeRequest.getMovieId());
        Room room = roomService.findRoomById(showtimeRequest.getRoomId());
        LocalDateTime startDateTime = DateUtil.stringToLocalDateTime(showtimeRequest.getStartDateTime());

        return Showtime.builder()
                .movie(movie)
                .room(room)
                .startDateTime(startDateTime)
                .price(showtimeRequest.getPrice())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkIsShowtimeIsReserveable(String showtimeId) {
        findShowtimeById(showtimeId);
        return showtimeRepository.checkIsShowtimeIsReserveable(UUID.fromString(showtimeId));
    }
}
