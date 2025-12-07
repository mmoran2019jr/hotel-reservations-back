package com.mario.hotel.service;

import com.mario.hotel.dto.RoomRequestDTO;
import com.mario.hotel.dto.RoomResponseDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface RoomService {

    RoomResponseDTO createRoom(RoomRequestDTO request);

    RoomResponseDTO updateRoom(String roomId, RoomRequestDTO request);

    void deleteRoom(String roomId);

    RoomResponseDTO getRoomById(String roomId);

    Page<RoomResponseDTO> getRooms(
            String type,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean onlyAvailable,
            int page,
            int size
    );
}
