package org.imannuel.moviereservationapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.mapper.PaymentMapper;
import org.imannuel.moviereservationapi.dto.mapper.ReservationMapper;
import org.imannuel.moviereservationapi.dto.request.reservation.ReservationRequest;
import org.imannuel.moviereservationapi.dto.response.payment.PaymentResponse;
import org.imannuel.moviereservationapi.dto.response.reservation.ReservationPageResponse;
import org.imannuel.moviereservationapi.dto.response.reservation.ReservationResponse;
import org.imannuel.moviereservationapi.entity.Payment;
import org.imannuel.moviereservationapi.entity.Reservation;
import org.imannuel.moviereservationapi.entity.Showtime;
import org.imannuel.moviereservationapi.entity.UserAccount;
import org.imannuel.moviereservationapi.repository.ReservationRepository;
import org.imannuel.moviereservationapi.service.PaymentService;
import org.imannuel.moviereservationapi.service.ReservationService;
import org.imannuel.moviereservationapi.service.ShowtimeService;
import org.imannuel.moviereservationapi.utils.PaginationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final PaymentService paymentService;

    private final ShowtimeService showtimeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse createReservation(ReservationRequest reservationRequest) {
        if (!showtimeService.checkIsShowtimeIsReserveable(reservationRequest.getShowtimeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Showtime is not reserveable");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();

        Showtime showtime = showtimeService.findShowtimeById(reservationRequest.getShowtimeId());
        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID())
                .showtime(showtime)
                .user(userAccount)
                .isCancel(false)
                .build();
        reservationRepository.insertReservation(reservation);

        for (String seatId : reservationRequest.getSeatId()) {
            if (!checkIsSeatAvailable(seatId, showtime.getId().toString())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat is not available");
            }
            createSeatReservation(reservation.getId().toString(), seatId);
        }

        Payment payment = paymentService.createPayment(reservation, reservationRequest);
        reservation.setPayment(payment);
        reservationRepository.updateReservationPayment(reservation);
        return PaymentMapper.paymentToPaymentResponse(payment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSeatReservation(String reservationId, String seatId) {
        reservationRepository.insertSeatReservation(UUID.fromString(reservationId), UUID.fromString(seatId));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkIsSeatAvailable(String seatId, String showtimeId) {
        return reservationRepository.checkIsSeatAvailable(UUID.fromString(seatId), UUID.fromString(showtimeId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelReservation(String reservationId) {
        Reservation reservation = findReservationById(reservationId);
        if (reservation.getIsCancel()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation cant cancelled because its already cancel");
        }
        reservationRepository.cancelReservation(UUID.fromString(reservationId));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkIsReservationIsCancelable(String reservationId) {
        boolean isCancelable = reservationRepository.checkIsReservationIsCancelable(UUID.fromString(reservationId));
        if (!isCancelable) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation cant be canceled");
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation findReservationById(String reservationId) {
        return reservationRepository.findReservationById(UUID.fromString(reservationId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getReservationById(String reservationId) {
        Reservation reservation = findReservationById(reservationId);
        return ReservationMapper.reservationToReservationResponse(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationPageResponse getAllReservationByUserId(Integer page, Integer size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();

        int offset = PaginationUtil.calculateOffset(page, size);
        long totalReservations = reservationRepository.countTotalReservationByUserId(userAccount.getId());
        int totalPages = PaginationUtil.calculateTotalPages(totalReservations, size);

        List<Reservation> reservationByUserId = reservationRepository.getAllReservationByUserId(userAccount.getId(), size, offset);

        return ReservationPageResponse.builder()
                .reservations(ReservationMapper.reservationListToReservationListResponse(reservationByUserId))
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalReservations)
                .totalPages(totalPages)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByReservationIdAndUserAccountId(String reservationId, String userAccountId) {
        return reservationRepository.existsByReservationIdIdAndUserAccountId(UUID.fromString(reservationId), UUID.fromString(userAccountId));
    }
}