package com.mario.hotel.service;

import com.mario.hotel.dto.AuthResponseDTO;
import com.mario.hotel.dto.LoginRequestDTO;
import com.mario.hotel.dto.UserRegistrationRequestDTO;

public interface AuthService {

    AuthResponseDTO register(UserRegistrationRequestDTO request);

    AuthResponseDTO login(LoginRequestDTO request);
}
