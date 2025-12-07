package com.mario.hotel.repository;

import com.mario.hotel.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {

    List<Reservation> findByUserId(String userId);

    List<Reservation> findByRoomIdAndStatusIn(String roomId, List<String> statuses);
}
