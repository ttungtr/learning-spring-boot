package com.example.democrud.crud.service;

import com.example.democrud.crud.dto.request.ChatMessageRequestDto;
import com.example.democrud.crud.dto.response.ChatMessageResponseDto;

import java.util.List;

public interface ChatService {

    ChatMessageResponseDto sendMessage(String username, ChatMessageRequestDto request);

    List<ChatMessageResponseDto> getConversation(Long userId, Long otherId);

    void markConversationAsRead(Long userId, Long otherId);

}


