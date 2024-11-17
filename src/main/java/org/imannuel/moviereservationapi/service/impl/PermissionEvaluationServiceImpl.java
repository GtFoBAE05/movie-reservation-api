package org.imannuel.moviereservationapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.imannuel.moviereservationapi.service.PermissionEvaluationService;
import org.imannuel.moviereservationapi.service.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PermissionEvaluationServiceImpl implements PermissionEvaluationService {
    private final ReservationService reservationService;

    @Override
    @Transactional(readOnly = true)
    public boolean hasAccessToReservation(String reservationId, String userAccountId) {
        return reservationService.existsByReservationIdAndUserAccountId(reservationId, userAccountId);
    }
}
