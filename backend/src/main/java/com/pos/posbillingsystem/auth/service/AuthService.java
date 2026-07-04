package com.pos.posbillingsystem.auth.service;

import org.springframework.stereotype.Service;

import com.pos.posbillingsystem.auth.dto.LoginRequest;
import com.pos.posbillingsystem.auth.dto.LoginResponse;
import com.pos.posbillingsystem.security.JwtService;
import com.pos.posbillingsystem.user.entity.User;
import com.pos.posbillingsystem.user.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid Username"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponse(
                token,
                user.getUsername(),
                user.getRole().name()
        );
    }
}