package com.example.democrud.crud.websocket;

import com.example.democrud.crud.entity.UserEntity;
import com.example.democrud.crud.repository.UserRepository;
import com.example.democrud.crud.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        try {
            String token = extractToken(request);
            if (token == null) {
                log.warn("WebSocket handshake missing token");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
            String username = jwtTokenUtil.getUsernameFromToken(token);
            if (username == null) {
                log.warn("WebSocket handshake invalid token for username {}", username);
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            // Build minimal UserDetails to reuse validateToken
            var userDetails = User.withUsername(username)
                    .password("") // not used
                    .authorities("USER")
                    .build();
            if (!jwtTokenUtil.validateToken(token, userDetails)) {
                log.warn("WebSocket handshake token validation failed for username {}", username);
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            UserEntity user = userRepository.findByEmail(username);
            if (user == null) {
                log.warn("WebSocket handshake user not found {}", username);
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            attributes.put("userId", user.getId());
            attributes.put("username", user.getEmail());
            return true;
        } catch (Exception ex) {
            log.error("WebSocket handshake error", ex);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
    }

    private String extractToken(ServerHttpRequest request) {
        // Try Authorization header
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        // Fallback: token query param
        var params = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        String tokenParam = params.getFirst("token");
        if (tokenParam != null && !tokenParam.isBlank()) {
            return tokenParam;
        }
        return null;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // no-op
    }
}


