package com.example.democrud.crud.dto.response;

import lombok.*;

import java.time.Instant;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {
    private Long id;
    private Long senderId;
    private Long recipientId;
    private String content;
    private Boolean isRead;
    private Instant createdDate;
}


