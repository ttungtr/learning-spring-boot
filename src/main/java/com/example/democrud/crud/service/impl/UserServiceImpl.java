package com.example.democrud.crud.service.impl;


import com.example.democrud.crud.dto.request.UserRequestDto;
import com.example.democrud.crud.dto.response.UserResponseDto;
import com.example.democrud.crud.entity.UserEntity;
import com.example.democrud.crud.repository.UserRepository;
import com.example.democrud.crud.service.UserService;
import com.example.democrud.crud.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl  implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    @Transactional
    public void createUser(UserRequestDto request) {


        userRepository.save(
                UserEntity.builder()
                        .email(request.getEmail())
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .build());
    }

    @Override
    public UserResponseDto getUserById (Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));


        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<UserEntity> list = userRepository.findAll();
        return list.stream().map(userMapper::userToUserResponseDto).collect(Collectors.toList());
    };
}
