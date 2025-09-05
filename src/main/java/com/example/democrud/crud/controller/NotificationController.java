package com.example.democrud.crud.controller;

import com.example.democrud.crud.dto.request.NotificationRequestDto;
import com.example.democrud.crud.dto.response.CountNotificationsResponseDto;
import com.example.democrud.crud.dto.response.NotificationResponseDto;
import com.example.democrud.crud.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("")

    public ResponseEntity<List<NotificationResponseDto>> getNotifications(@RequestParam("userId") Long userId)
    {
        List<NotificationResponseDto> notifications = notificationService.findAllByUserId(userId);

        return ResponseEntity.ok()
                .body(notifications);
    }

    @PostMapping("")
    public ResponseEntity<Void> createNotification(@RequestParam("userId") Long userId, @RequestBody  NotificationRequestDto notificationRequestDto) {
        notificationService.createNotification(userId, notificationRequestDto);

        return ResponseEntity.ok().build();
    }
    @GetMapping("/unread")
    public ResponseEntity<CountNotificationsResponseDto> getNotificationUnreadByUserId(@RequestParam("userId") Long userId) {
        CountNotificationsResponseDto  notifications = notificationService.getAllNotificationUnread(userId);

        return ResponseEntity.ok()
                .body(notifications);
    }

    @PutMapping("read/{id}")
    public ResponseEntity<Void> makeReadNotification(@RequestParam("userId") Long userId, @PathVariable("id") Long notificationId) {
        notificationService.makeReadNotification(userId, notificationId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("readAll")
    public ResponseEntity<Void> makeReadAllNotification(@RequestParam("userId") Long userId) {
        notificationService.makeReadAllNotification(userId);
        return ResponseEntity.ok().build();
    }

}
