package com.example.democrud.crud.controller;

import com.example.democrud.crud.dto.request.AuthRequest;
import com.example.democrud.crud.dto.response.AuthResponse;
import com.example.democrud.crud.dto.request.RegisterRequest;
import com.example.democrud.crud.repository.UserRepository;
import com.example.democrud.crud.security.JwtTokenUtil;
import com.example.democrud.crud.service.AuthService;
import com.example.democrud.crud.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse  response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
             throw new ApiException("Email already exists", HttpStatus.BAD_REQUEST);
        }
        authService.register(request);
        return ResponseEntity.noContent().build();
    }
}


