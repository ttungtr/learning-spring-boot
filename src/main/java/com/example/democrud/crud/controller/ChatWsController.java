package com.example.democrud.crud.controller;

import com.example.democrud.crud.dto.request.ChatMessageRequestDto;
import com.example.democrud.crud.dto.request.ChatSendWsDto;
import com.example.democrud.crud.dto.response.ChatMessageResponseDto;
import com.example.democrud.crud.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // Client sends to /app/chat.send
    @MessageMapping("/chat.send")
    public void send(@Payload ChatSendWsDto request) {
        ChatMessageRequestDto req = ChatMessageRequestDto.builder()
                .recipientId(request.getRecipientId())
                .content(request.getContent())
                .build();
        ChatMessageResponseDto dto = chatService.sendMessage(request.getSenderId(), req);
        messagingTemplate.convertAndSend("/topic/chat." + request.getRecipientId(), dto);
    }
}


