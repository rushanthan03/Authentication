package com.spring.auth.dto.response;

import com.spring.auth.common.RoleType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse extends SimpleResponse {

    private String email;
    private String userName;
    private String token;
    private Long userId;
    private RoleType role;

}
