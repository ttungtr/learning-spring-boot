package com.example.democrud.crud.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String accessToken = extractAccessTokenFromRequest(request);
        String username = null;
        boolean isTokenExpired = false;

        // Đọc và validate accessToken
        if (accessToken != null) {
            try {
                username = jwtTokenUtil.getUsernameFromToken(accessToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired, attempting auto-refresh");
                isTokenExpired = true;
                // Vẫn lấy username từ expired token
                username = e.getClaims().getSubject();
            }
        }

        // Nếu token hết hạn, tự động refresh
        if (isTokenExpired && username != null) {
            String newAccessToken = attemptAutoRefresh(request, response, username);
            if (newAccessToken != null) {
                // Sử dụng accessToken mới
                accessToken = newAccessToken;
                isTokenExpired = false;
            }
        }

        // Validate và set authentication nếu token hợp lệ
        if (username != null && !isTokenExpired && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(accessToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Đọc accessToken từ cookie (ưu tiên) hoặc header Authorization
     */
    private String extractAccessTokenFromRequest(HttpServletRequest request) {
        // Ưu tiên đọc từ cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // Fallback: đọc từ header Authorization
        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            return requestTokenHeader.substring(7);
        }

        return null;
    }

    /**
     * Tự động refresh accessToken từ refreshToken trong cookie
     */
    private String attemptAutoRefresh(HttpServletRequest request, HttpServletResponse response, String username) {
        try {
            // Đọc refreshToken từ cookie
            Cookie[] cookies = request.getCookies();
            String refreshToken = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("refreshToken".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }

            if (refreshToken == null || refreshToken.isEmpty()) {
                System.out.println("Refresh token not found in cookie");
                return null;
            }

            // Validate refreshToken
            if (!jwtTokenUtil.validateRefreshToken(refreshToken)) {
                System.out.println("Refresh token is invalid or expired");
                return null;
            }

            // Lấy userDetails để tạo token mới
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                System.out.println("User not found: " + username);
                return null;
            }

            // Tạo accessToken mới
            String newAccessToken = jwtTokenUtil.generateToken(userDetails);

            // Set accessToken mới vào cookie
            Cookie accessTokenCookie = new Cookie("accessToken", newAccessToken);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(false); // Set true nếu dùng HTTPS
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(86400); // 24 hours
            response.addCookie(accessTokenCookie);

            System.out.println("Access token auto-refreshed successfully for user: " + username);
            return newAccessToken;

        } catch (Exception e) {
            System.out.println("Error during auto-refresh: " + e.getMessage());
            return null;
        }
    }
}
