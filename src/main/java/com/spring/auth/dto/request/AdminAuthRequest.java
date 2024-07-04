package com.spring.auth.dto.request;

import lombok.Data;

@Data
public class AdminAuthRequest {

    private String email;
    private String password;
}
