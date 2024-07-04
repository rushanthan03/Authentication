package com.spring.auth.service;


import com.spring.auth.dto.request.ChangePasswordDto;
import com.spring.auth.dto.request.UserPasswordResetRequest;
import com.spring.auth.dto.response.OtpResponse;
import com.spring.auth.dto.response.SimpleResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

public interface OTPLoginService {

    ResponseEntity<Map<String, String>> verifyUser(String phone, String otp, String token);

    ResponseEntity<OtpResponse> getOtp(String phoneNumber);

    String generateOtp();



}
