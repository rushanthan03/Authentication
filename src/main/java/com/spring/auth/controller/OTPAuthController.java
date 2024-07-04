package com.spring.auth.controller;

import com.spring.auth.dto.response.OtpResponse;
import com.spring.auth.service.OTPLoginService;
import com.spring.auth.util.EndpointURI;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("*")
@RestController
@SecurityRequirement(name = "user-service")
@RequiredArgsConstructor
public class OTPAuthController {

    private final OTPLoginService otpLoginService;

    @PostMapping(value = EndpointURI.LOGIN)
    public ResponseEntity<OtpResponse> login(@RequestParam("phone_no") String phoneNo) {
        return otpLoginService.getOtp(phoneNo);
    }

    @PostMapping(value = EndpointURI.OTP_VERIFICATION)
    public ResponseEntity<Map<String, String>> otpVerification(
            @RequestParam("phone_no") String phoneNo,
            @RequestParam("otp") String otp,
            @RequestParam("token") String token) {

        return otpLoginService.verifyUser(phoneNo, otp, token);
    }
}
