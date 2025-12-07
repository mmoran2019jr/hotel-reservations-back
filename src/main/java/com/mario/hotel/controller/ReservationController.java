package com.mario.hotel.controller;

import com.mario.hotel.dto.ReservationRequestDTO;
import com.mario.hotel.dto.ReservationResponseDTO;
import com.mario.hotel.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ReservationResponseDTO createReservation(@Valid @RequestBody ReservationRequestDTO request) {
        return reservationService.createReservation(request);
    }

    @GetMapping("/user/{userId}")
    public List<ReservationResponseDTO> getReservationsByUser(@PathVariable String userId) {
        return reservationService.getReservationsByUser(userId);
    }

    @PutMapping("/{id}")
    public ReservationResponseDTO updateReservation(@PathVariable("id") String id,
                                                    @Valid @RequestBody ReservationRequestDTO request) {
        return reservationService.updateReservation(id, request);
    }

    @DeleteMapping("/{id}")
    public void cancelReservation(@PathVariable("id") String id) {
        reservationService.cancelReservation(id);
    }

    @PostMapping("/{id}/checkout")
    public ReservationResponseDTO checkout(@PathVariable("id") String id) {
        return reservationService.checkoutReservation(id);
    }
}
