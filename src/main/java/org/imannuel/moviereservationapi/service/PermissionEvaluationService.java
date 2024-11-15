package org.imannuel.moviereservationapi.service;

public interface PermissionEvaluationService {
    boolean hasAccessToReservation(String reservationId, String userAccountId);
}
