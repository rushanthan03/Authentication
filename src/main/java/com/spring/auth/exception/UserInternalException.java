package com.spring.auth.exception;

import lombok.Getter;

@Getter
public class UserInternalException extends RuntimeException {
    private final Error error;
    private final String additionalInfo;

    public UserInternalException(Error error, String additionalInfo) {
        super(additionalInfo);
        this.error = error;
        this.additionalInfo = additionalInfo;
    }

    public UserInternalException(Error error, Throwable ex) {
        super(ex.getMessage());
        this.error = error;
        this.additionalInfo = ex.getLocalizedMessage();
    }



}