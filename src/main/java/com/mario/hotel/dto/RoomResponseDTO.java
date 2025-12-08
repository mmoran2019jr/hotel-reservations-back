package com.mario.hotel.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class RoomResponseDTO {

    private String id;
    private String name;
    private String type;
    private BigDecimal pricePerNight;
    private Integer capacity;
    private List<String> amenities;
    private List<String> imageUrls;
    private Boolean active;
    
}