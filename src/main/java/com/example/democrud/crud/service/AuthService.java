package com.example.democrud.crud.service;

import com.example.democrud.crud.dto.request.AuthRequest;
import com.example.democrud.crud.dto.request.RegisterRequest;
import com.example.democrud.crud.dto.response.AuthResponse;

public interface AuthService {

    void register(RegisterRequest request);

    AuthResponse login(AuthRequest request);
}
