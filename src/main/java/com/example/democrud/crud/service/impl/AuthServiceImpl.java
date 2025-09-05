package com.example.democrud.crud.service.impl;

import com.example.democrud.crud.common.ApiException;
import com.example.democrud.crud.dto.request.AuthRequest;
import com.example.democrud.crud.dto.request.RegisterRequest;
import com.example.democrud.crud.dto.response.AuthResponse;
import com.example.democrud.crud.entity.UserEntity;
import com.example.democrud.crud.repository.UserRepository;
import com.example.democrud.crud.security.JwtTokenUtil;
import com.example.democrud.crud.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    
    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new ApiException("Email already exists", HttpStatus.BAD_REQUEST);
        }
        
        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
    }
    
    @Override
    public AuthResponse login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            throw new ApiException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return AuthResponse.builder().token(token).build();
    }
}
