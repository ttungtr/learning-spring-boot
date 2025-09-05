package com.example.democrud.crud.service;

import com.example.democrud.crud.dto.request.NotificationRequestDto;


import com.example.democrud.crud.dto.response.CountNotificationsResponseDto;
import com.example.democrud.crud.dto.response.NotificationResponseDto;

import java.util.List;


public interface NotificationService {

    List<NotificationResponseDto> findAllByUserId(Long userId);

    void createNotification(Long userId, NotificationRequestDto notificationRequestDto);

    CountNotificationsResponseDto getAllNotificationUnread(Long userId);

    void makeReadNotification(Long userId, Long id);

    void makeReadAllNotification(Long userId);

}
