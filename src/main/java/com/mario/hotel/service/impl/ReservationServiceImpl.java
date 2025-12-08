package com.mario.hotel.service.impl;

import com.mario.hotel.dto.ReservationRequestDTO;
import com.mario.hotel.dto.ReservationResponseDTO;
import com.mario.hotel.dto.RoomResponseDTO;
import com.mario.hotel.exception.ResourceNotFoundException;
import com.mario.hotel.model.Reservation;
import com.mario.hotel.model.Room;
import com.mario.hotel.repository.ReservationRepository;
import com.mario.hotel.repository.RoomRepository;
import com.mario.hotel.repository.UserRepository;
import com.mario.hotel.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

	@Override
	public ReservationResponseDTO createReservation(ReservationRequestDTO request) {
	    var user = userRepository.findById(request.getUserId())
	            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

	    var room = roomRepository.findById(request.getRoomId())
	            .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId()));

	    if (Boolean.FALSE.equals(room.getActive())) {
	        throw new IllegalStateException("Room is not active");
	    }

	    long nights = calculateNights(request.getCheckInDate(), request.getCheckOutDate());

	    // Busca las reservas de la habitacion por id de room
	    var activeStatuses = List.of(STATUS_PENDING, STATUS_CONFIRMED);
	    var existingReservations = reservationRepository.findByRoomIdAndStatusIn(room.getId(), activeStatuses);

	    boolean conflict = existingReservations.stream().anyMatch(r ->
	            hasDateConflict(
	                    request.getCheckInDate(),
	                    request.getCheckOutDate(),
	                    r.getCheckInDate(),
	                    r.getCheckOutDate()
	            )
	    );

	    if (conflict) {
	        throw new IllegalStateException("Room is not available for the selected dates");
	    }

	    var totalPrice = room.getPricePerNight().multiply(java.math.BigDecimal.valueOf(nights));

	    var reservation = Reservation.builder()
	            .userId(user.getId())
	            .roomId(room.getId())
	            .checkInDate(request.getCheckInDate())
	            .checkOutDate(request.getCheckOutDate())
	            .totalPrice(totalPrice)
	            .status(STATUS_CONFIRMED) // o PENDING si quieres otro flujo
	            .archived(false)
	            .build();

	    var saved = reservationRepository.save(reservation);

	    return mapToDTO(saved);
	}


	@Override
	public List<ReservationResponseDTO> getReservationsByUser(String userId) {
	    var reservations = reservationRepository.findByUserId(userId);
	    return reservations.stream()
	            .map(this::mapToDTO)
	            .toList();
	}


	@Override
	public ReservationResponseDTO updateReservation(String reservationId, ReservationRequestDTO request) {
	    var reservation = reservationRepository.findById(reservationId)
	            .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

	    if (STATUS_CANCELLED.equals(reservation.getStatus()) || STATUS_CHECKED_OUT.equals(reservation.getStatus())) {
	        throw new IllegalStateException("Cannot modify a cancelled or checked-out reservation");
	    }

	    var room = roomRepository.findById(request.getRoomId())
	            .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId()));

	    long nights = calculateNights(request.getCheckInDate(), request.getCheckOutDate());

	    // Checar conflictos de fechas (ignorando la misma reserva)
	    var activeStatuses = List.of(STATUS_PENDING, STATUS_CONFIRMED);
	    var existingReservations = reservationRepository.findByRoomIdAndStatusIn(room.getId(), activeStatuses);

	    boolean conflict = existingReservations.stream()
	            .filter(r -> !r.getId().equals(reservation.getId()))
	            .anyMatch(r ->
	                    hasDateConflict(
	                            request.getCheckInDate(),
	                            request.getCheckOutDate(),
	                            r.getCheckInDate(),
	                            r.getCheckOutDate()
	                    )
	            );

	    if (conflict) {
	        throw new IllegalStateException("Room is not available for the selected dates");
	    }

	    var totalPrice = room.getPricePerNight().multiply(java.math.BigDecimal.valueOf(nights));

	    reservation.setRoomId(room.getId());
	    reservation.setUserId(request.getUserId());
	    reservation.setCheckInDate(request.getCheckInDate());
	    reservation.setCheckOutDate(request.getCheckOutDate());
	    reservation.setTotalPrice(totalPrice);

	    var updated = reservationRepository.save(reservation);
	    return mapToDTO(updated);
	}


	@Override
	public void cancelReservation(String reservationId) {
	    var reservation = reservationRepository.findById(reservationId)
	            .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

	    if (STATUS_CANCELLED.equals(reservation.getStatus())) {
	        return; // ya cancelada
	    }

	    reservation.setStatus(STATUS_CANCELLED);
	    reservationRepository.save(reservation);
	}


	@Override
	public ReservationResponseDTO checkoutReservation(String reservationId) {
	    var reservation = reservationRepository.findById(reservationId)
	            .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

	    if (STATUS_CANCELLED.equals(reservation.getStatus())) {
	        throw new IllegalStateException("Cannot checkout a cancelled reservation");
	    }

	    if (STATUS_CHECKED_OUT.equals(reservation.getStatus())) {
	        return mapToDTO(reservation); // ya estaba finalizada
	    }

	    reservation.setStatus(STATUS_CHECKED_OUT);
	    reservation.setArchived(true);

	    var updated = reservationRepository.save(reservation);


	    return mapToDTO(updated);
	}

    
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_CONFIRMED = "CONFIRMED";
    private static final String STATUS_CANCELLED = "CANCELLED";
    private static final String STATUS_CHECKED_OUT = "CHECKED_OUT";

    private long calculateNights(LocalDate checkIn, LocalDate checkOut) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        if (days <= 0) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        return days;
    }

    private boolean hasDateConflict(LocalDate newStart, LocalDate newEnd,
                                    LocalDate existingStart, LocalDate existingEnd) {
        // rango de fechas
        return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
    }
    
   //metodo de mapeo de dto
	private ReservationResponseDTO mapToDTO(Reservation reservation) {
		
        var room = roomRepository
                .findById(reservation.getRoomId())
                .orElse(null); // Busca la habitacion por usuario
        
        
	    return ReservationResponseDTO.builder()
	            .id(reservation.getId())
	            .userId(reservation.getUserId())
	            .roomId(reservation.getRoomId())
	            .roomName(room != null ? room.getName() : null)
	            .checkInDate(reservation.getCheckInDate())
	            .checkOutDate(reservation.getCheckOutDate())
	            .totalPrice(reservation.getTotalPrice())
	            .status(reservation.getStatus())
	            .archived(reservation.getArchived())
	            .build();
	}

}
