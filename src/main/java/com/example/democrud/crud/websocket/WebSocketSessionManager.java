package com.example.democrud.crud.websocket;

import com.example.democrud.crud.dto.response.ChatMessageResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketSessionManager {

    private final ObjectMapper objectMapper;

    // Map userId -> Set of WebSocket sessions
    private final Map<Long, Set<WebSocketSession>> userSessions = new ConcurrentHashMap<>();

    public void addSession(Long userId, WebSocketSession session) {
        userSessions.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(session);
        log.info("WebSocket session added for user {}: {}", userId, session.getId());
    }

    public void removeSession(Long userId, WebSocketSession session) {
        Set<WebSocketSession> sessions = userSessions.get(userId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                userSessions.remove(userId);
            }
            log.info("WebSocket session removed for user {}: {}", userId, session.getId());
        }
    }

    /**
     * Push message to all WebSocket sessions of a user
     */
    public void pushMessageToUser(Long userId, ChatMessageResponseDto message) {
        Set<WebSocketSession> sessions = userSessions.get(userId);
        if (sessions == null || sessions.isEmpty()) {
            log.debug("No WebSocket sessions for user {}", userId);
            return;
        }

        try {
            String json = objectMapper.writeValueAsString(message);
            TextMessage textMessage = new TextMessage(json);

            sessions.removeIf(session -> {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(textMessage);
                        return false;
                    } else {
                        return true; // Remove closed session
                    }
                } catch (Exception e) {
                    log.error("Error sending message to session", e);
                    return true; // Remove failed session
                }
            });

            log.info("Pushed message to {} sessions of user {}", sessions.size(), userId);
        } catch (Exception e) {
            log.error("Error pushing message to user {}", userId, e);
        }
    }
}


