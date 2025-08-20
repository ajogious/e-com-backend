package com.ajogious.e_com_backend.services;

import com.ajogious.e_com_backend.dtos.AuthResponse;
import com.ajogious.e_com_backend.dtos.LoginRequest;
import com.ajogious.e_com_backend.dtos.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}