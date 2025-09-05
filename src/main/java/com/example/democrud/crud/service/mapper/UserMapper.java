package com.example.democrud.crud.service.mapper;


import com.example.democrud.crud.dto.response.UserResponseDto;
import com.example.democrud.crud.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserMapper {
    public UserResponseDto userToUserResponseDto(UserEntity userEntity) {
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .build();
    }
}
