package com.example.democrud.crud.controller;


import com.example.democrud.crud.dto.request.UserRequestDto;
import com.example.democrud.crud.dto.response.UserResponseDto;
import com.example.democrud.crud.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<Void> createUser(@RequestBody UserRequestDto user) {
        userService.createUser(user);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> user =  userService.getAllUsers();
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/{id}" )
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        UserResponseDto user = userService.getUserById(id);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponseDto user = userService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok().body(user);
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        userService.logout(response);
        return ResponseEntity.noContent().build();
    }
}
