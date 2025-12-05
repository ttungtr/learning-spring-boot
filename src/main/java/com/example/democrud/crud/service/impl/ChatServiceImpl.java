package com.example.democrud.crud.service.impl;

import com.example.democrud.crud.common.ApiException;
import com.example.democrud.crud.dto.request.ChatMessageRequestDto;
import com.example.democrud.crud.dto.response.ChatMessageResponseDto;
import com.example.democrud.crud.entity.ChatMessageEntity;
import com.example.democrud.crud.entity.UserEntity;
import com.example.democrud.crud.repository.ChatMessageRepository;
import com.example.democrud.crud.repository.UserRepository;
import com.example.democrud.crud.service.ChatService;
import com.example.democrud.crud.service.mapper.ChatMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import com.example.democrud.crud.websocket.WebSocketSessionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final WebSocketSessionManager sessionManager;

    @Override
    @Transactional
    public ChatMessageResponseDto sendMessage(String senderUsername, ChatMessageRequestDto request) {
        UserEntity sender = userRepository.findByEmail(senderUsername);
        if (sender == null) {
            throw new ApiException("Sender not found", HttpStatus.BAD_REQUEST);
        }
        UserEntity recipient = userRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new ApiException("Recipient not found", HttpStatus.BAD_REQUEST));

        ChatMessageEntity saved = chatMessageRepository.save(ChatMessageEntity.builder()
                .sender(sender)
                .recipient(recipient)
                .content(request.getContent())
                .build());

        ChatMessageResponseDto dto = chatMessageMapper.toDto(saved);

        // Push to recipient via raw WebSocket
        System.out.println("Pushing message to user " + recipient.getId() + " via WebSocket");
        sessionManager.pushMessageToUser(recipient.getId(), dto);
        System.out.println("Message pushed successfully");

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponseDto> getConversation(Long userId, Long otherId) {
        return chatMessageRepository.findConversation(userId, otherId)
                .stream().map(chatMessageMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markConversationAsRead(Long userId, Long otherId) {
        chatMessageRepository.markConversationAsRead(userId, otherId);
    }


}


