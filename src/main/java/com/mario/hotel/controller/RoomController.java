package com.mario.hotel.controller;

import com.mario.hotel.dto.RoomRequestDTO;
import com.mario.hotel.dto.RoomResponseDTO;
import com.mario.hotel.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public RoomResponseDTO createRoom(@Valid @RequestBody RoomRequestDTO request) {
        return roomService.createRoom(request);
    }

    @PutMapping("/{id}")
    public RoomResponseDTO updateRoom(@PathVariable("id") String id,
                                      @Valid @RequestBody RoomRequestDTO request) {
        return roomService.updateRoom(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable("id") String id) {
        roomService.deleteRoom(id);
    }

    @GetMapping("/{id}")
    public RoomResponseDTO getRoomById(@PathVariable("id") String id) {
        return roomService.getRoomById(id);
    }

    @GetMapping
    public Page<RoomResponseDTO> getRooms(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return roomService.getRooms(type, minPrice, maxPrice, onlyAvailable, page, size);
    }
}
