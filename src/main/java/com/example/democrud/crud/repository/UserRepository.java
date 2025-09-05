package com.example.democrud.crud.repository;

import com.example.democrud.crud.entity.UserEntity;
import com.example.democrud.crud.entity.UserNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
}
