package com.example.democrud.crud.controller;

import com.example.democrud.crud.dto.request.ChatMessageRequestDto;
import com.example.democrud.crud.dto.response.ChatMessageResponseDto;
import com.example.democrud.crud.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
class ChatController {

    private final ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<ChatMessageResponseDto> send(@AuthenticationPrincipal UserDetails userDetails,
                                                       @Validated @RequestBody ChatMessageRequestDto request) {
        return ResponseEntity.ok(chatService.sendMessage(userDetails.getUsername(), request));
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<ChatMessageResponseDto>> conversation(@RequestParam("userId") Long userId,
                                                                     @RequestParam("otherId") Long otherId) {
        return ResponseEntity.ok(chatService.getConversation(userId, otherId));
    }

    @PutMapping("/read")
    public ResponseEntity<Void> markRead(@RequestParam("userId") Long userId,
                                         @RequestParam("otherId") Long otherId) {
        chatService.markConversationAsRead(userId, otherId);
        return ResponseEntity.ok().build();
    }


}


