package com.spring.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {
    private String code;
    private String message;
    @JsonProperty("additional_info")
    private String additionalInfo;
}
