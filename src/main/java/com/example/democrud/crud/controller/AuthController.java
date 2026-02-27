package com.example.democrud.crud.controller;

import com.example.democrud.crud.dto.request.AuthRequest;
import com.example.democrud.crud.dto.response.AuthResponse;
import com.example.democrud.crud.dto.request.RegisterRequest;
import com.example.democrud.crud.repository.UserRepository;
import com.example.democrud.crud.security.JwtTokenUtil;
import com.example.democrud.crud.service.AuthService;
import com.example.democrud.crud.common.ApiException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
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
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request, HttpServletResponse httpResponse) {
        AuthResponse response = authService.login(request);
        
        // Set accessToken vào httpOnly cookie
        Cookie accessTokenCookie = new Cookie("accessToken", response.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false); // Set true nếu dùng HTTPS
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(86400); // 24 hours
        httpResponse.addCookie(accessTokenCookie);
        
        // Set refreshToken vào httpOnly cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", response.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // Set true nếu dùng HTTPS
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(604800); // 7 days
        httpResponse.addCookie(refreshTokenCookie);
        
        // Không trả token trong response body, FE không cần biết
        response.setAccessToken(null);
        response.setRefreshToken(null);
        
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

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse httpResponse) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new ApiException("Refresh token is missing", HttpStatus.UNAUTHORIZED);
        }
        
        AuthResponse response = authService.refreshToken(refreshToken);
        
        // Set accessToken mới vào cookie
        Cookie accessTokenCookie = new Cookie("accessToken", response.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false); // Set true nếu dùng HTTPS
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(86400); // 24 hours
        httpResponse.addCookie(accessTokenCookie);
        
        // Cập nhật refreshToken mới vào cookie nếu có
        if (response.getRefreshToken() != null) {
            Cookie refreshTokenCookie = new Cookie("refreshToken", response.getRefreshToken());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(false); // Set true nếu dùng HTTPS
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(604800); // 7 days
            httpResponse.addCookie(refreshTokenCookie);
        }
        
        // Không trả token trong response body
        response.setAccessToken(null);
        response.setRefreshToken(null);
        
        return ResponseEntity.ok(response);
    }

}


