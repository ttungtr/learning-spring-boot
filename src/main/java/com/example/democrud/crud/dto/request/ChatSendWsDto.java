package com.example.democrud.crud.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatSendWsDto {

    @NotNull
    private Long senderId;

    @NotNull
    private Long recipientId;

    @NotBlank
    private String content;
}


