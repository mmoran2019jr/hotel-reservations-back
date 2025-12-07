package com.mario.hotel.service;

import com.mario.hotel.dto.ReservationRequestDTO;
import com.mario.hotel.dto.ReservationResponseDTO;

import java.util.List;

public interface ReservationService {

    ReservationResponseDTO createReservation(ReservationRequestDTO request);

    List<ReservationResponseDTO> getReservationsByUser(String userId);

    ReservationResponseDTO updateReservation(String reservationId, ReservationRequestDTO request);

    void cancelReservation(String reservationId);

    ReservationResponseDTO checkoutReservation(String reservationId);
}
