package com.mario.hotel.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "Room")
@Getter
@Setter
@Builder                    
@NoArgsConstructor          
@AllArgsConstructor         // Constructor
public class Room {

    @Id
    private String id;

    private String name;           
    private String type;           // "SIMPLE", "DOBLE", "SUITE"
    private BigDecimal pricePerNight;
    private Integer capacity;

    private List<String> amenities;
    private List<String> imageUrls;

    private Boolean active = true; 
}