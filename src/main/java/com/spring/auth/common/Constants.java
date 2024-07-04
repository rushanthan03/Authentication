package com.spring.auth.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    @UtilityClass
    public static class ErrorCode {
        public static final String USER_NOT_FOUND_ERROR_CODE = "100";
        public static final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found";

        public static final String INTERNAL_SERVER_ERROR_CODE = "900";
        public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
    }
}
