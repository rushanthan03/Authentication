package com.spring.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpResponse {

    private String token;
    private Boolean isOldAccount;
}
