package com.spring.auth.exception;

import com.spring.auth.dto.response.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OtpGenerationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Example status code; adjust as needed
    public ResponseEntity<SimpleResponse> handleOtpGenerationException(OtpGenerationException ex) {
        log.error("Exception while generating OTP: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(SimpleResponse.builder()
                        .message("OTP generation failed, please try again")
                        .success(false)
                        .build());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // Example status code; adjust as needed
    public ResponseEntity<SimpleResponse> handleAuthenticationException(AuthenticationException ex) {
        log.error("Authentication exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(SimpleResponse.builder()
                        .message("Invalid OTP")
                        .success(false)
                        .build());
    }

    @ExceptionHandler(UserInternalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Example status code; adjust as needed
    public ResponseEntity<SimpleResponse> handleUserInternalException(UserInternalException ex) {
        log.error("Internal exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(SimpleResponse.builder()
                        .message(ex.getError().getMessage())
                        .success(false)
                        .build());
    }

    // Add more @ExceptionHandler methods as needed for other exceptions

}
