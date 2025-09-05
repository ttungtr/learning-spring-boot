package com.example.democrud.crud.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name= "t_user_notification")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationEntity extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead =  false ;

    @Column(name = "detail", nullable = false)
    private String detail;

    @Column(name = "sender")
    @Builder.Default
    private String sender = "System";


    @JoinColumn(name = "fk_user_id", nullable = false, referencedColumnName="id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;


    @Column(name = "language")
    @Builder.Default
    private String language = "en";

    @Column(name = "is_push")
    @Builder.Default
    private Boolean isPush = false;

    @Column(name = "is_sms")
    @Builder.Default
    private Boolean isSMS = false;

    @Column(name = "is_bell")
    @Builder.Default
    private Boolean isBell = false;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;



}
