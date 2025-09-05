package com.example.democrud.crud.service;

import com.example.democrud.crud.dto.request.UserRequestDto;
import com.example.democrud.crud.dto.response.UserResponseDto;
import com.example.democrud.crud.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDto getUserById(Long id);

    void createUser(UserRequestDto user);

    List<UserResponseDto> getAllUsers();

}
