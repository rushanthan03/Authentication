package com.spring.auth.dto.request;

import lombok.Data;

@Data
public class UserPasswordResetRequest {

    private String otp;
    private String password;
}
