package com.example.aihealthmanagement.service;


import com.example.aihealthmanagement.dto.AuthDto.LoginRequest;
import com.example.aihealthmanagement.dto.AuthDto.LoginResponse;
import com.example.aihealthmanagement.dto.AuthDto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}

