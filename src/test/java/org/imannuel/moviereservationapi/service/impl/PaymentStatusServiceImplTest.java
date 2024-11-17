package org.imannuel.moviereservationapi.service.impl;

import org.imannuel.moviereservationapi.entity.PaymentStatus;
import org.imannuel.moviereservationapi.repository.PaymentStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PaymentStatusServiceImplTest {
    @Mock
    private PaymentStatusRepository paymentStatusRepository;

    @InjectMocks
    private PaymentStatusServiceImpl paymentStatusService;

    @Test
    void shouldCallInsertPaymentStatusWhenCreatePaymentStatus() {
        String paymentStatusName = "PENDING";
        Mockito.when(paymentStatusRepository.existsPaymentStatusByName(paymentStatusName)).thenReturn(false);

        paymentStatusService.createPaymentStatus(paymentStatusName);

        Mockito.verify(paymentStatusRepository, Mockito.times(1))
                .existsPaymentStatusByName(paymentStatusName);
        Mockito.verify(paymentStatusRepository, Mockito.times(1))
                .insertPaymentStatus(paymentStatusName);
    }

    @Test
    void shouldThrowErrorWhenCreatePaymentStatus() {
        String paymentStatusName = "PENDING";
        Mockito.when(paymentStatusRepository.existsPaymentStatusByName(paymentStatusName)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> paymentStatusService.createPaymentStatus(paymentStatusName));

        assertEquals("Payment status already exists", exception.getReason());
        Mockito.verify(paymentStatusRepository, Mockito.times(1))
                .existsPaymentStatusByName(paymentStatusName);
    }

    @Test
    void shouldReturnPaymentStatusWhenFindPaymentStatusByName() {
        String paymentStatusName = "PENDING";
        PaymentStatus expectedPaymentStatus = PaymentStatus.builder()
                .id(1)
                .name(paymentStatusName)
                .build();
        Mockito.when(paymentStatusRepository.findPaymentStatusByName(paymentStatusName))
                .thenReturn(Optional.of(expectedPaymentStatus));

        PaymentStatus paymentStatus = paymentStatusService.findPaymentStatusByName(paymentStatusName);

        assertEquals(expectedPaymentStatus.getId(), paymentStatus.getId());
        assertEquals(expectedPaymentStatus.getName(), paymentStatus.getName());

        Mockito.verify(paymentStatusRepository, Mockito.times(1))
                .findPaymentStatusByName(paymentStatusName);
    }

    @Test
    void shouldThrowErrorWhenFindPaymentStatusByName() {
        String paymentStatusName = "PENDING";
        Mockito.when(paymentStatusRepository.findPaymentStatusByName(paymentStatusName))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> paymentStatusService.findPaymentStatusByName(paymentStatusName));

        assertEquals("Payment status not found", exception.getReason());


        Mockito.verify(paymentStatusRepository, Mockito.times(1))
                .findPaymentStatusByName(paymentStatusName);
    }
}