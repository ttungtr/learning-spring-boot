package com.example.democrud.crud.service.impl;
import com.example.democrud.crud.common.ApiException;
import com.example.democrud.crud.dto.request.NotificationRequestDto;
import com.example.democrud.crud.dto.response.CountNotificationsResponseDto;
import com.example.democrud.crud.entity.UserEntity;
import com.example.democrud.crud.entity.UserNotificationEntity;
import com.example.democrud.crud.repository.UserNotificationRepository;
import com.example.democrud.crud.repository.UserRepository;
import com.example.democrud.crud.service.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import com.example.democrud.crud.dto.response.NotificationResponseDto;
import com.example.democrud.crud.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final UserNotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    private final UserRepository userEntityRepository;

    @Override
    @Transactional
    public List<NotificationResponseDto> findAllByUserId(Long userId) {

        List<UserNotificationEntity> notifications = notificationRepository.findAllByUserId(userId);
        if (notifications.isEmpty()) return List.of();
        return notifications.stream().map(notificationMapper::notificationToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createNotification(Long userId, NotificationRequestDto request) {
        UserEntity user =  userEntityRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

         notificationRepository.save(
                UserNotificationEntity.builder()
                        .title(request.getTitle())
                        .message(request.getMessage())
                        .detail(request.getDetail())
                        .user(user).build()
        );
    }

    @Override
    @Transactional
    public CountNotificationsResponseDto getAllNotificationUnread (Long  userId) {

          Long count = notificationRepository.countByUserIdAndIsReadFalse(userId);
          return CountNotificationsResponseDto.builder().count(count).build();
    }


    @Override
    @Transactional
    public void makeReadNotification(Long  userId, Long notificationId) {

        Optional<UserNotificationEntity> notification = notificationRepository.findByIdAndUserId(notificationId, userId);

        if(notification.isEmpty()){

            throw new ApiException("Notification not found!", HttpStatus.BAD_REQUEST);

        }

        notification.ifPresent(noti -> {
            noti.setIsRead(true);
            notificationRepository.save(noti);
        });
    }

    @Override
    @Transactional
    public void makeReadAllNotification(Long  userId) {
         notificationRepository.markAllAsReadByUserId(userId);
    }

}
