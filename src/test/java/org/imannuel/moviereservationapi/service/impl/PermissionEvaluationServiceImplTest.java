package org.imannuel.moviereservationapi.service.impl;

import org.imannuel.moviereservationapi.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PermissionEvaluationServiceImplTest {
    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private PermissionEvaluationServiceImpl permissionEvaluationService;

    @Test
    void shouldReturnTrueWhenHasAccessToReservation() {
        String reservationId = UUID.randomUUID().toString();
        String userAccountId = UUID.randomUUID().toString();

        Mockito.when(reservationService.existsByReservationIdAndUserAccountId(reservationId, userAccountId))
                .thenReturn(true);

        boolean hasAccessToReservation = permissionEvaluationService.hasAccessToReservation(reservationId, userAccountId);

        assertTrue(hasAccessToReservation);

        Mockito.verify(reservationService, Mockito.times(1)).existsByReservationIdAndUserAccountId(reservationId, userAccountId);
    }
}