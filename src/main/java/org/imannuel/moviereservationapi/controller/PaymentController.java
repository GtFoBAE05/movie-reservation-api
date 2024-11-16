package org.imannuel.moviereservationapi.controller;


import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.midtrans.MidtransNotificationRequest;
import org.imannuel.moviereservationapi.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping(path = "/notifications")
    public ResponseEntity handleNotification(@RequestBody MidtransNotificationRequest request) {
        paymentService.receivePaymentNotification(request);
        return ApiMapper.basicMapper(HttpStatus.OK, "OK", null);
    }

}
