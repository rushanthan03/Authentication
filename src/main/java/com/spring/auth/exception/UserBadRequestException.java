package com.spring.auth.exception;

public class UserBadRequestException extends UserInternalException {

    public UserBadRequestException(Error error, String additionalInfo) {
        super(error,additionalInfo);
    }

    public UserBadRequestException(Error error, Exception ex) {
        super(error, ex.getMessage());
    }
}
