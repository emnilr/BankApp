package com.s8mil.bank_webapp.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String contraseña;
}
