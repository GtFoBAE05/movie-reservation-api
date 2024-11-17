package org.imannuel.moviereservationapi.service.impl;

import org.imannuel.moviereservationapi.dto.mapper.ReservationMapper;
import org.imannuel.moviereservationapi.dto.request.reservation.ReservationRequest;
import org.imannuel.moviereservationapi.dto.response.payment.PaymentResponse;
import org.imannuel.moviereservationapi.dto.response.reservation.ReservationPageResponse;
import org.imannuel.moviereservationapi.dto.response.reservation.ReservationResponse;
import org.imannuel.moviereservationapi.entity.*;
import org.imannuel.moviereservationapi.repository.ReservationRepository;
import org.imannuel.moviereservationapi.service.PaymentService;
import org.imannuel.moviereservationapi.service.ShowtimeService;
import org.imannuel.moviereservationapi.utils.PaginationUtil;
import org.imannuel.moviereservationapi.utils.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ShowtimeService showtimeService;

    @Mock
    private ValidationUtil validationUtil;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private Authentication authentication;

    @Test
    void shouldReturnPaymentResponseWhenCreateReservation() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount userAccount = UserAccount.builder()
                .id(UUID.randomUUID())
                .username("admin")
                .build();
        Mockito.when(authentication.getPrincipal()).thenReturn(userAccount);
        Showtime showtime = Showtime.builder()
                .id(UUID.randomUUID())
                .room(Room.builder().build())
                .movie(Movie.builder()
                        .id(UUID.randomUUID())
                        .genres(List.of())
                        .images(List.of())
                        .releaseDate(LocalDate.now())
                        .build())
                .price(50000L)
                .startDateTime(LocalDateTime.parse("2024-11-17T15:30:45"))
                .build();

        ReservationRequest reservationRequest = ReservationRequest.builder()
                .showtimeId(showtime.getId().toString())
                .seatId(List.of(UUID.randomUUID().toString()))
                .build();

        PaymentStatus paymentStatus = PaymentStatus.builder()
                .id(1)
                .name("PENDING")
                .build();

        Payment payment = Payment.builder()
                .id(UUID.randomUUID())
                .amount(50000L)
                .paymentStatus(paymentStatus)
                .redirectUrl("www")
                .tokenSnap("tokensnap")
                .build();

        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID())
                .showtime(showtime)
                .user(userAccount)
                .isCancel(false)
                .payment(payment)
                .build();

        payment.setReservation(reservation);

        PaymentResponse expectedPayment = PaymentResponse.builder()
                .reservationId(reservation.getId().toString())
                .amount(payment.getAmount())
                .tokenSnap(payment.getTokenSnap())
                .redirectUrl(payment.getRedirectUrl())
                .paymentStatus(paymentStatus.getName())
                .build();

        Mockito.when(showtimeService.checkIsShowtimeIsReserveable(showtime.getId().toString())).thenReturn(true);
        Mockito.when(showtimeService.findShowtimeById(showtime.getId().toString())).thenReturn(showtime);
        Mockito.doNothing().when(reservationRepository).insertReservation(Mockito.any(Reservation.class));
        Mockito.when(reservationRepository.checkIsSeatAvailable(
                UUID.fromString(reservationRequest.getSeatId().get(0)),
                UUID.fromString(reservationRequest.getShowtimeId()))).thenReturn(true);
        Mockito.doReturn(payment).when(paymentService).createPayment(
                Mockito.any(Reservation.class),
                Mockito.any(ReservationRequest.class)
        );
        Mockito.doNothing().when(reservationRepository).updateReservationPayment(Mockito.any(Reservation.class));

        PaymentResponse paymentResponse = reservationService.createReservation(reservationRequest);

        assertEquals(expectedPayment.getTokenSnap(), paymentResponse.getTokenSnap());
        assertEquals(expectedPayment.getPaymentStatus(), paymentResponse.getPaymentStatus());
        assertEquals(expectedPayment.getRedirectUrl(), paymentResponse.getRedirectUrl());
        assertEquals(expectedPayment.getAmount(), paymentResponse.getAmount());
        Mockito.verify(reservationRepository, Mockito.times(1))
                .insertReservation(Mockito.any(Reservation.class));
        Mockito.verify(reservationRepository, Mockito.times(1))
                .updateReservationPayment(Mockito.any(Reservation.class));
    }

    @Test
    void shouldThrowErrorWHenCreateReservation() {
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .showtimeId(UUID.randomUUID().toString())
                .seatId(List.of(UUID.randomUUID().toString()))
                .build();
        Mockito.when(showtimeService.checkIsShowtimeIsReserveable(reservationRequest.getShowtimeId())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> reservationService.createReservation(reservationRequest));

        assertEquals("Showtime is not reserveable", exception.getReason());
        Mockito.verify(showtimeService, Mockito.times(1)).checkIsShowtimeIsReserveable(reservationRequest.getShowtimeId());
    }

    @Test
    void shouldThrowErrorWhenCreateListReservation() {
        Showtime showtime = Showtime.builder()
                .id(UUID.randomUUID())
                .room(Room.builder().build())
                .movie(Movie.builder()
                        .id(UUID.randomUUID())
                        .genres(List.of())
                        .images(List.of())
                        .releaseDate(LocalDate.now())
                        .build())
                .price(50000L)
                .startDateTime(LocalDateTime.parse("2024-11-17T15:30:45"))
                .build();

        ReservationRequest reservationRequest = ReservationRequest.builder()
                .showtimeId(showtime.getId().toString())
                .seatId(List.of(UUID.randomUUID().toString()))
                .build();

        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID())
                .showtime(showtime)
                .isCancel(false)
                .build();

        Mockito.when(reservationRepository.checkIsSeatAvailable(
                UUID.fromString(reservationRequest.getSeatId().get(0)),
                UUID.fromString(reservationRequest.getShowtimeId()))).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> reservationService.createSeatListReservation(reservation, reservationRequest));

        assertEquals("Seat is not available", exception.getReason());
    }

    @Test
    void shouldCallCancelReservationWhenCancelReservation() {
        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID())
                .isCancel(false)
                .build();
        Mockito.when(reservationRepository.findReservationById(reservation.getId())).thenReturn(Optional.of(reservation));

        reservationService.cancelReservation(reservation.getId().toString());

        Mockito.verify(reservationRepository, Mockito.times(1)).cancelReservation(reservation.getId());
    }

    @Test
    void shouldThrowErrorWhenCancelReservation() {
        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID())
                .isCancel(true)
                .build();
        Mockito.when(reservationRepository.findReservationById(reservation.getId())).thenReturn(Optional.of(reservation));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> reservationService.cancelReservation(reservation.getId().toString()));

        assertEquals("Reservation cant cancelled because its already cancel", exception.getReason());
    }

    @Test
    void shouldReturnReservationResponseWhenGetReservationById() {
        Reservation expectedReservation = Reservation.builder()
                .id(UUID.randomUUID())
                .isCancel(true)
                .showtime(Showtime.builder()
                        .id(UUID.randomUUID())
                        .room(Room.builder().build())
                        .movie(Movie.builder()
                                .id(UUID.randomUUID())
                                .genres(List.of())
                                .images(List.of())
                                .releaseDate(LocalDate.now())
                                .build())
                        .price(50000L)
                        .startDateTime(LocalDateTime.parse("2024-11-17T15:30:45"))
                        .build())
                .seats(List.of())
                .build();

        Mockito.when(reservationRepository.findReservationById(expectedReservation.getId())).thenReturn(Optional.of(expectedReservation));

        ReservationResponse reservationById = reservationService.getReservationById(expectedReservation.getId().toString());

        assertEquals(expectedReservation.getId().toString(), reservationById.getId());
        assertEquals(expectedReservation.getIsCancel(), reservationById.isCancelStatus());
    }

    @Test
    void shouldReturnAllReservationByUserIdWhenGetAllReservationByUserId() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount userAccount = UserAccount.builder()
                .id(UUID.randomUUID())
                .username("admin")
                .build();
        Mockito.when(authentication.getPrincipal()).thenReturn(userAccount);
        int page = 0;
        int size = 10;
        int offset = PaginationUtil.calculateOffset(page, size);
        long totalElements = 1L;
        int totalPages = PaginationUtil.calculateTotalPages(totalElements, size);

        List<Reservation> reservationList = List.of(
                Reservation.builder()
                        .id(UUID.randomUUID())
                        .isCancel(true)
                        .showtime(Showtime.builder()
                                .id(UUID.randomUUID())
                                .room(Room.builder().build())
                                .movie(Movie.builder()
                                        .id(UUID.randomUUID())
                                        .genres(List.of())
                                        .images(List.of())
                                        .releaseDate(LocalDate.now())
                                        .build())
                                .price(50000L)
                                .startDateTime(LocalDateTime.parse("2024-11-17T15:30:45"))
                                .build())
                        .seats(List.of())
                        .user(userAccount)
                        .build()
        );

        ReservationPageResponse expectedReservationPageResponse = ReservationPageResponse.builder()
                .reservations(ReservationMapper.reservationListToReservationListResponse(reservationList))
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();

        Mockito.when(reservationRepository.getAllReservationByUserId(userAccount.getId(), size, offset)).thenReturn(reservationList);
        Mockito.when(reservationRepository.countTotalReservationByUserId(userAccount.getId())).thenReturn(totalElements);

        ReservationPageResponse reservationPageResponse = reservationService.getAllReservationByUserId(page, size);
        assertEquals(expectedReservationPageResponse.getReservations().size(), reservationPageResponse.getReservations().size());
        assertEquals(expectedReservationPageResponse.getTotalElements(), reservationPageResponse.getTotalElements());
        assertEquals(expectedReservationPageResponse.getTotalPages(), reservationPageResponse.getTotalPages());
        assertEquals(expectedReservationPageResponse.getCurrentPage(), reservationPageResponse.getCurrentPage());
        assertEquals(expectedReservationPageResponse.getPageSize(), reservationPageResponse.getPageSize());

        Mockito.verify(reservationRepository, Mockito.times(1)).getAllReservationByUserId(userAccount.getId(), size, offset);
        Mockito.verify(reservationRepository, Mockito.times(1)).countTotalReservationByUserId(userAccount.getId());
    }

    @Test
    void shouldReturnBooleanWhenExistsByReservationIdAndUserAccountId() {
        UUID reservationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Mockito.when(reservationRepository.existsByReservationIdIdAndUserAccountId(reservationId, userId)).thenReturn(true);

        boolean result = reservationService.existsByReservationIdAndUserAccountId(reservationId.toString(), userId.toString());

        assertTrue(result);
        Mockito.verify(reservationRepository, Mockito.times(1)).existsByReservationIdIdAndUserAccountId(reservationId, userId);
    }
}