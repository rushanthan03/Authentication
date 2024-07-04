package com.spring.auth.controller;

import com.spring.auth.dto.request.AdminAuthRequest;
import com.spring.auth.dto.request.ChangePasswordDto;
import com.spring.auth.dto.request.UserPasswordResetRequest;
import com.spring.auth.dto.response.SimpleResponse;
import com.spring.auth.service.EmailLoginService;
import com.spring.auth.util.EndpointURI;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@CrossOrigin("*")
@RestController
@SecurityRequirement(name = "user-service")
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailLoginService emailLoginService;

    @PostMapping(EndpointURI.ADMIN_AUTH)
    public ResponseEntity<SimpleResponse> adminLogin(@RequestBody AdminAuthRequest request) {
        return emailLoginService.adminLogin(request);
    }

    @PostMapping(value = EndpointURI.FORGOT_PASSWORD)
    public ResponseEntity<SimpleResponse> forgotPassword(@PathVariable("email") String email, HttpServletRequest request) throws MessagingException {
        return emailLoginService.forgotPassword(email, request);
    }

    @GetMapping(value = EndpointURI.IS_FORGOT_PASSWORD_OTP_VALID)
    public ResponseEntity<SimpleResponse> checkForgotPasswordToken(@PathVariable("otp") String otp) {
        return emailLoginService.checkValidationForForgotPasswordOtp(otp);
    }

    @PutMapping(value = EndpointURI.USER_PASSWORD_RESET)
    public ResponseEntity<SimpleResponse> resetPassword(@RequestBody UserPasswordResetRequest userPasswordResetRequest) throws MessagingException {
        return emailLoginService.resetUserPassword(userPasswordResetRequest);
    }

    @PutMapping(value = EndpointURI.CHANGE_PASSWORD)
    public ResponseEntity<SimpleResponse> changePassword(@RequestBody ChangePasswordDto changePasswordDto, Principal principal) {
        return emailLoginService.changePassword(changePasswordDto, principal);
    }

}
