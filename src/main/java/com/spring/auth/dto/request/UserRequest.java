package com.spring.auth.dto.request;

import com.spring.auth.common.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRequest {

    @Schema(name = "userName", example = "kilman", required = true)
    private String userName;
    @Schema(name = "email", example = "kilmanantony@gmail.com", required = true)
    private String email;
    @Schema(name = "phoneNo", example = "763751320", required = true)
    private String phoneNo;
    @Schema(name = "dateOfBirth", example = "1999.05.11", required = true)
    private String dateOfBirth;
    @Schema(name = "gender", example = "male", required = true)
    private String gender;
    private RoleType role;

}
