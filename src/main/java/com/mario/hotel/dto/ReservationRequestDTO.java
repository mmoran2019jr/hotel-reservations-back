package com.mario.hotel.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRequestDTO {

    @NotBlank
    private String roomId;

    @NotBlank
    private String userId;

    @NotNull
    @FutureOrPresent
    private LocalDate checkInDate;

    @NotNull
    @FutureOrPresent
    private LocalDate checkOutDate;
}
