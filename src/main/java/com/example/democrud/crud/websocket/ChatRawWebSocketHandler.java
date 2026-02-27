package com.example.democrud.crud.websocket;

import com.example.democrud.crud.dto.request.ChatMessageRequestDto;
import com.example.democrud.crud.dto.response.ChatMessageResponseDto;
import com.example.democrud.crud.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatRawWebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private final ObjectMapper objectMapper;
    private final WebSocketSessionManager sessionManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        String username = (String) session.getAttributes().get("username");
        if (userId == null || username == null) {
            log.warn("WebSocket connection missing auth attributes, closing");
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        sessionManager.addSession(userId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId == null) {
            log.warn("Received message from session without userId");
            return;
        }

        log.info("WebSocket message from user {}: {}", userId, message.getPayload());

        try {
            // Parse JSON: {"recipientId":4,"content":"hello"}
            ChatMessageRequestDto request = objectMapper.readValue(message.getPayload(), ChatMessageRequestDto.class);
            
            String username = (String) session.getAttributes().get("username");
            if (username == null) {
                log.warn("No username in WebSocket session attributes");
                session.sendMessage(new TextMessage("{\"error\":\"username required\"}"));
                return;
            }

            // Send message via service (saves to DB and returns DTO)
            ChatMessageResponseDto dto = chatService.sendMessage(username, request);

            // Push to recipient's WebSocket sessions
//            sessionManager.pushMessageToUser(dto.getRecipientId(), dto);

        } catch (Exception e) {
            log.error("Error handling WebSocket message", e);
            session.sendMessage(new TextMessage("{\"error\":\"" + e.getMessage() + "\"}"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            sessionManager.removeSession(userId, session);
        }
    }
}

