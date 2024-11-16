package org.imannuel.moviereservationapi.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.request.midtrans.MidtransNotificationRequest;
import org.imannuel.moviereservationapi.dto.response.template.ApiTemplateResponse;
import org.imannuel.moviereservationapi.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "APIs for managing payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Handle payment notifications",
            description = "Handles payment notificationsfrom Midtrans, updating the payment status based on the notification content.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Notification processed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid notification data", content = @Content(schema = @Schema(implementation = ApiTemplateResponse.class))),
            })

    @PostMapping(path = "/notifications")
    public ResponseEntity handleNotification(@RequestBody MidtransNotificationRequest request) {
        paymentService.receivePaymentNotification(request);
        return ApiMapper.basicMapper(HttpStatus.OK, "OK", null);
    }

}
