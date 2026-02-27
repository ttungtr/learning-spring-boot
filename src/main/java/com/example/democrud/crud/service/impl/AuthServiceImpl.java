package com.example.democrud.crud.service.impl;

import com.example.democrud.crud.common.ApiException;
import com.example.democrud.crud.dto.request.AuthRequest;
import com.example.democrud.crud.dto.request.RegisterRequest;
import com.example.democrud.crud.dto.response.AuthResponse;
import com.example.democrud.crud.dto.response.UserResponseDto;
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
        String accessToken = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        UserEntity userEntity = userRepository.findByEmail(userDetails.getUsername());

        UserResponseDto user = UserResponseDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .build();
        return AuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken).user(user).build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        // Validate refreshToken
        if (!jwtTokenUtil.validateRefreshToken(refreshToken)) {
            throw new ApiException("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED);
        }

        // Get username from refreshToken
        String username = jwtTokenUtil.getUsernameFromRefreshToken(refreshToken);
        if (username == null) {
            throw new ApiException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UserEntity userEntity = userRepository.findByEmail(username);
        
        if (userEntity == null) {
            throw new ApiException("User not found", HttpStatus.UNAUTHORIZED);
        }

        // Generate new accessToken
        String newAccessToken = jwtTokenUtil.generateToken(userDetails);
        
        // Optionally generate new refreshToken (rotate refresh token)
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        UserResponseDto user = UserResponseDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .build();
        
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .user(user)
                .build();
    }
}
