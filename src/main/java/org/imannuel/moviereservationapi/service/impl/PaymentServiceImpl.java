package org.imannuel.moviereservationapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.imannuel.moviereservationapi.client.MidtransAppClient;
import org.imannuel.moviereservationapi.constant.Constant;
import org.imannuel.moviereservationapi.dto.mapper.PaymentMapper;
import org.imannuel.moviereservationapi.dto.request.midtrans.*;
import org.imannuel.moviereservationapi.dto.request.reservation.ReservationRequest;
import org.imannuel.moviereservationapi.dto.response.midtrans.MidtransSnapResponse;
import org.imannuel.moviereservationapi.dto.response.payment.PaymentResponse;
import org.imannuel.moviereservationapi.entity.Payment;
import org.imannuel.moviereservationapi.entity.PaymentStatus;
import org.imannuel.moviereservationapi.entity.Reservation;
import org.imannuel.moviereservationapi.repository.PaymentRepository;
import org.imannuel.moviereservationapi.service.PaymentService;
import org.imannuel.moviereservationapi.service.PaymentStatusService;
import org.imannuel.moviereservationapi.utils.HashUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final MidtransAppClient midtransAppClient;
    private final PaymentStatusService paymentStatusService;

    @Value("${midtrans.server.key}")
    private String MIDTRANS_SERVER_KEY;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Payment createPayment(Reservation reservation, ReservationRequest reservationRequest) {
        MidtransSnapResponse midtransSnapResponse = buildMidtransPaymentRequest(reservation, reservationRequest);
        Payment payment = Payment.builder()
                .id(UUID.randomUUID())
                .amount(calculateReservationPrice(reservation, reservationRequest))
                .paymentStatus(paymentStatusService.findPaymentStatusByName("PENDING"))
                .tokenSnap(midtransSnapResponse.getToken())
                .redirectUrl(midtransSnapResponse.getRedirectUrl())
                .reservation(reservation)
                .build();
        paymentRepository.insertPayment(payment);
        return payment;
    }

    @Override
    @Transactional(readOnly = true)
    public Payment findPaymentById(String paymentId){
        return paymentRepository.findPaymentById(UUID.fromString(paymentId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Payment findPaymentByReservationId(String paymentId){
        log.info(paymentId);
        return paymentRepository.findPaymentByReservationId(UUID.fromString(paymentId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePaymentStatus(String reservationId, String toPaymentStatus) {
        Payment payment = findPaymentByReservationId(reservationId);
        PaymentStatus newPaymentStatus = paymentStatusService.findPaymentStatusByName(toPaymentStatus);
        payment.setPaymentStatus(newPaymentStatus);

        if(newPaymentStatus.getName().equals("SETTLEMENT")){
            payment.getReservation().setIsCancel(true);
        }

        paymentRepository.updatePayment(payment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receivePaymentNotification(MidtransNotificationRequest midtransNotificationRequest) {
        log.info("Start getNotification: {}", System.currentTimeMillis());
        validateSignatureKey(midtransNotificationRequest.getOrderId(), midtransNotificationRequest.getStatusCode(), midtransNotificationRequest.getGrossAmount(), midtransNotificationRequest.getSignatureKey());
        updatePaymentStatus(midtransNotificationRequest.getOrderId(), midtransNotificationRequest.getTransactionStatus());
        log.info("End getNotification: {}", System.currentTimeMillis());

    }

    private MidtransSnapResponse buildMidtransPaymentRequest(Reservation reservation, ReservationRequest reservationRequest ){
        List<MidtransItemDetailRequest> itemDetailRequests = List.of(
                MidtransItemDetailRequest.builder()
                        .id(reservation.getShowtime().getId().toString())
                        .name("Ticket of %s".formatted(reservation.getShowtime().getMovie().getTitle()))
                        .price(reservation.getShowtime().getPrice())
                        .quantity(reservationRequest.getSeatId().size())
                        .build()
        );

        MidtransCustomerDetailsRequest customerDetailsRequest = MidtransCustomerDetailsRequest.builder()
                .email(reservation.getUser().getEmail())
                .build();

        MidtransTransactionRequest transactionRequest = MidtransTransactionRequest.builder()
                .orderId(reservation.getId().toString())
                .grossAmount(calculateReservationPrice(reservation, reservationRequest))
                .build();

        MidtransPaymentRequest paymentRequest = MidtransPaymentRequest.builder()
                .transactionDetail(transactionRequest)
                .enabledPayments(Constant.enabledPayments)
                .itemDetails(itemDetailRequests)
                .customerDetails(customerDetailsRequest)
                .build();
        String headerValue = "Basic " + Base64.getEncoder().encodeToString(MIDTRANS_SERVER_KEY.getBytes(StandardCharsets.UTF_8));
        return midtransAppClient.createSnapTransaction(paymentRequest, headerValue);
    }

    private Long calculateReservationPrice(Reservation reservation, ReservationRequest reservationRequest){
        return reservationRequest.getSeatId().size() * reservation.getShowtime().getPrice();
    }

    private void validateSignatureKey(String orderId, String statusCode, String grossAmount, String midtransSignatureKey) {
        String rawString = orderId + statusCode + grossAmount + MIDTRANS_SERVER_KEY;
        log.info("0   "+ orderId);
        log.info("0   "+ statusCode);
        log.info("0   "+ grossAmount);
        log.info("0   " + MIDTRANS_SERVER_KEY);

        String signatureKey = HashUtil.encryptMidtransKey(rawString);
        log.info("1   "+ signatureKey);
        log.info("2   "+ midtransSignatureKey);

        if (!signatureKey.equalsIgnoreCase(midtransSignatureKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature key");
        }
    }

}
