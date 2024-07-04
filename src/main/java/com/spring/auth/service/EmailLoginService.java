package com.spring.auth.service;


import com.spring.auth.dto.request.AdminAuthRequest;
import com.spring.auth.dto.request.ChangePasswordDto;
import com.spring.auth.dto.request.UserPasswordResetRequest;
import com.spring.auth.dto.response.OtpResponse;
import com.spring.auth.dto.response.SimpleResponse;
import com.spring.auth.entity.VerificationToken;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

public interface EmailLoginService {

    ResponseEntity<SimpleResponse> adminLogin(AdminAuthRequest adminAuthRequest);

    ResponseEntity<SimpleResponse> forgotPassword(String email, HttpServletRequest request)throws MessagingException;

    ResponseEntity<SimpleResponse> checkValidationForForgotPasswordOtp(String otp);

    ResponseEntity<SimpleResponse> resetUserPassword(UserPasswordResetRequest userPasswordResetRequest)throws MessagingException;

    ResponseEntity<SimpleResponse> changePassword(ChangePasswordDto changePasswordDto, Principal principal);

    public String getPassword(String email);

    public boolean isMailExists(String email);

    public VerificationToken getUserByToken(String token);

    public boolean isTokenExist(String token);
}
