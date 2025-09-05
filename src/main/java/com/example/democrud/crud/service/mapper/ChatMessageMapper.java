package com.example.democrud.crud.service.mapper;

import com.example.democrud.crud.dto.response.ChatMessageResponseDto;
import com.example.democrud.crud.entity.ChatMessageEntity;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageMapper {

    public ChatMessageResponseDto toDto(ChatMessageEntity entity) {
        if (entity == null) return null;
        return ChatMessageResponseDto.builder()
                .id(entity.getId())
                .senderId(entity.getSender().getId())
                .recipientId(entity.getRecipient().getId())
                .content(entity.getContent())
                .isRead(entity.getIsRead())
                .createdDate(entity.getCreatedDate())
                .build();
    }
}


