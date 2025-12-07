package com.mario.hotel.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data  // ¡Esto genera TODO: getters, setters, toString, equals, hashCode!   
public class RoomRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El tipo es obligatorio")
    private String type;

    @NotNull(message = "El precio por noche es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal pricePerNight;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad mínima es 1 persona")
    private Integer capacity;

    private List<String> amenities;

    private List<String> imageUrls;

    private Boolean active = true;   // valor por defecto opcional
}