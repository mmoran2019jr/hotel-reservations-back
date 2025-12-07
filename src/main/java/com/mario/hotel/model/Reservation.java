package com.mario.hotel.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collection = "Reservation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    private String id;

    private String userId;   // referencia a User
    private String roomId;   // referencia a Room

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private BigDecimal totalPrice;
    private String status;   // "PENDING", "CONFIRMED", "CANCELLED", "CHECKED_OUT"

    private Boolean archived; // para marcar historial

	public String getUserId() {
		return userId;
	}

}
