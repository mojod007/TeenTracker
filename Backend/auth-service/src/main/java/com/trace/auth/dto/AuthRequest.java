package com.trace.auth.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private String redirect;
}
