package com.mario.hotel.service.impl;

import com.mario.hotel.dto.AuthResponseDTO;
import com.mario.hotel.dto.LoginRequestDTO;
import com.mario.hotel.dto.UserRegistrationRequestDTO;
import com.mario.hotel.exception.ResourceNotFoundException;
import com.mario.hotel.model.User;
import com.mario.hotel.repository.UserRepository;
import com.mario.hotel.security.JwtTokenProvider;
import com.mario.hotel.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResponseDTO register(UserRegistrationRequestDTO request) {

        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new IllegalStateException("Email is already in use");
                });

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        User saved = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(saved.getEmail(), saved.getRole());

        return AuthResponseDTO.builder()
                .token(token)
                .userId(saved.getId())
                .email(saved.getEmail())
                .role(saved.getRole())
                .build();
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole());

        return AuthResponseDTO.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
