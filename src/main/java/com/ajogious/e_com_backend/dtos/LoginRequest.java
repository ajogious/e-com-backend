package com.ajogious.e_com_backend.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
