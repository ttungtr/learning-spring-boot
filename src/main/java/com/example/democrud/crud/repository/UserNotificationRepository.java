package com.example.democrud.crud.repository;

import com.example.democrud.crud.entity.UserNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserNotificationRepository extends JpaRepository<UserNotificationEntity, Long> {


    List<UserNotificationEntity> findAllByUserId(Long userId);


    Long countByUserIdAndIsReadFalse(Long userId);


    List<UserNotificationEntity>  findAllByUserIdAndIsReadFalse(Long  userId);


    Optional<UserNotificationEntity> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query("UPDATE UserNotificationEntity noti SET noti.isRead = TRUE WHERE noti.user.id = :userId AND noti.isRead = false ")
    void markAllAsReadByUserId(@Param("userId") Long userId);

}
