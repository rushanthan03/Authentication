package com.spring.auth.dto.response;

import com.spring.auth.common.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponse {
    private String username;
    private String phoneNo;
    private RoleType roleType;
    private Map<String, String> profileImageUrl;


}
