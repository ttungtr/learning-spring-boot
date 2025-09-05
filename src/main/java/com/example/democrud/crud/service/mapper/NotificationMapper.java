package com.example.democrud.crud.service.mapper;

import com.example.democrud.crud.dto.response.NotificationResponseDto;
import com.example.democrud.crud.entity.UserNotificationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationMapper {
    public NotificationResponseDto notificationToDto(UserNotificationEntity userNotificationEntity) {

        return NotificationResponseDto.builder()
                .id(userNotificationEntity.getId())
                .title(userNotificationEntity.getTitle())
                .message(userNotificationEntity.getMessage())
                .detail(userNotificationEntity.getDetail())
                .userId(userNotificationEntity.getUser().getId())
                .isRead(userNotificationEntity.getIsRead())
                .build();

    }
}
