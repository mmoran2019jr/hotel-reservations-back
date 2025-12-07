package com.mario.hotel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {

    private String token;
    private String userId;
    private String email;
    private String role;
}
