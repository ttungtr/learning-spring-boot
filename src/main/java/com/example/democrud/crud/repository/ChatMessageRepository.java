package com.example.democrud.crud.repository;

import com.example.democrud.crud.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    @Query("SELECT m FROM ChatMessageEntity m WHERE (m.sender.id = :userId AND m.recipient.id = :otherId) OR (m.sender.id = :otherId AND m.recipient.id = :userId) ORDER BY m.createdDate ASC")
    List<ChatMessageEntity> findConversation(@Param("userId") Long userId, @Param("otherId") Long otherId);

    @Modifying
    @Query("UPDATE ChatMessageEntity m SET m.isRead = true WHERE m.sender.id = :otherId AND m.recipient.id = :userId AND m.isRead = false")
    void markConversationAsRead(@Param("userId") Long userId, @Param("otherId") Long otherId);
}


