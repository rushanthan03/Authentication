package com.spring.auth.dto.response;

import lombok.Data;

import java.util.Map;

@Data
public class UserResponse {
    private Long id;
    private String userName;
    private String email;
    private String phoneNo;
    private String dateOfBirth;
    private String gender;

}
