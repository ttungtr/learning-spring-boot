package com.example.democrud.crud.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_chat_message")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageEntity extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, length = 2048)
    private String content;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;

    @JoinColumn(name = "fk_sender_id", nullable = false, referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity sender;

    @JoinColumn(name = "fk_recipient_id", nullable = false, referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity recipient;
}


