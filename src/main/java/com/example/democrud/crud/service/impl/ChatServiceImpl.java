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
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public ChatMessageResponseDto sendMessage(Long senderId, ChatMessageRequestDto request) {
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ApiException("Sender not found", HttpStatus.BAD_REQUEST));
        UserEntity recipient = userRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new ApiException("Recipient not found", HttpStatus.BAD_REQUEST));

        ChatMessageEntity saved = chatMessageRepository.save(ChatMessageEntity.builder()
                .sender(sender)
                .recipient(recipient)
                .content(request.getContent())
                .build());

        ChatMessageResponseDto dto = chatMessageMapper.toDto(saved);

        // push to recipient topic
        messagingTemplate.convertAndSend("/topic/chat." + recipient.getId(), dto);

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


