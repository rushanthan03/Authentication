package com.spring.auth.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Error {

    USER_NOT_FOUND_ERROR(Constants.ErrorCode.USER_NOT_FOUND_ERROR_CODE, Constants.ErrorCode.USER_NOT_FOUND_ERROR_MESSAGE),
    INTERNAL_SERVER_ERROR(Constants.ErrorCode.INTERNAL_SERVER_ERROR_CODE, Constants.ErrorCode.INTERNAL_SERVER_ERROR_MESSAGE);

    private final String code;
    private final String message;
}
