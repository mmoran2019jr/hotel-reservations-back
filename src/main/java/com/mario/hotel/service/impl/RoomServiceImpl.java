package com.mario.hotel.service.impl;

import com.mario.hotel.dto.RoomRequestDTO;
import com.mario.hotel.dto.RoomResponseDTO;
import com.mario.hotel.exception.ResourceNotFoundException;
import com.mario.hotel.model.Room;
import com.mario.hotel.repository.RoomRepository;
import com.mario.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //inyeccion de dependencias
public class RoomServiceImpl implements RoomService {

    // room repository
    private final RoomRepository roomRepository;

    @Override
    public RoomResponseDTO createRoom(RoomRequestDTO request) {
        Room room = mapToEntity(request);
        Room saved = roomRepository.save(room);
        return mapToDTO(saved);
    }

    @Override
    public RoomResponseDTO updateRoom(String roomId, RoomRequestDTO request) {
        Room existing = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + roomId));

        // Actualización de room
        existing.setName(request.getName());
        existing.setType(request.getType());
        existing.setPricePerNight(request.getPricePerNight());
        existing.setCapacity(request.getCapacity());
        existing.setAmenities(request.getAmenities());
        existing.setImageUrls(request.getImageUrls());
        existing.setActive(request.getActive());

        return mapToDTO(roomRepository.save(existing));
    }

    @Override
    public void deleteRoom(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + roomId));
        roomRepository.delete(room);
    }

    @Override
    public RoomResponseDTO getRoomById(String roomId) {
        return roomRepository.findById(roomId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + roomId));
    }

    @Override
    public Page<RoomResponseDTO> getRooms(String type,
                                          BigDecimal minPrice,
                                          BigDecimal maxPrice,
                                          Boolean onlyAvailable,
                                          int page,
                                          int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("pricePerNight").ascending());

        Page<Room> roomPage = roomRepository.findAll(pageable);

        List<RoomResponseDTO> filtered = roomPage.getContent().stream()
                .filter(room -> onlyAvailable == null || !onlyAvailable || Boolean.TRUE.equals(room.getActive()))
                .filter(room -> type == null || type.isBlank() || Objects.equals(room.getType(), type))
                .filter(room -> minPrice == null || room.getPricePerNight().compareTo(minPrice) >= 0)
                .filter(room -> maxPrice == null || room.getPricePerNight().compareTo(maxPrice) <= 0)
                .map(this::mapToDTO)
                .toList();

        return new PageImpl<>(filtered, pageable, roomPage.getTotalElements());
    }

    // Mapeo de dto lombok
    private Room mapToEntity(RoomRequestDTO dto) {
        return Room.builder()
                .name(dto.getName())
                .type(dto.getType())
                .pricePerNight(dto.getPricePerNight())
                .capacity(dto.getCapacity())
                .amenities(dto.getAmenities())
                .imageUrls(dto.getImageUrls())
                .active(dto.getActive() != null ? dto.getActive() : true)
                .build();
    }

    private RoomResponseDTO mapToDTO(Room room) {
        return RoomResponseDTO.builder()
                .id(room.getId())
                .name(room.getName())
                .type(room.getType())
                .pricePerNight(room.getPricePerNight())
                .capacity(room.getCapacity())
                .amenities(room.getAmenities())
                .imageUrls(room.getImageUrls())
                .active(room.getActive())
                .build();
    }
}