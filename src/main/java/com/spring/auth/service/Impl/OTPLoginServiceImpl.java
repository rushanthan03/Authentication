package com.spring.auth.service.Impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.auth.dto.request.ChangePasswordDto;
import com.spring.auth.dto.request.UserPasswordResetRequest;
import com.spring.auth.dto.response.OtpResponse;
import com.spring.auth.dto.response.SimpleResponse;
import com.spring.auth.entity.AuthUserDetail;
import com.spring.auth.entity.User;
import com.spring.auth.exception.OtpGenerationException;
import com.spring.auth.repository.UserRepository;
import com.spring.auth.security.JwtUtil;
import com.spring.auth.security.OtpTokenUtil;
import com.spring.auth.service.OTPLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class OTPLoginServiceImpl implements OTPLoginService {

    private final UserRepository userRepository;

    @Autowired
    private OtpTokenUtil otpTokenUtil;

    @Autowired
    @Qualifier("webClientForTextNotificationService")
    private WebClient notifyWebClient;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ResponseEntity<Map<String, String>> verifyUser(String phone, String otp, String token) {
        Map<String, String> simpleResponse = new HashMap<>();
        if (userRepository.existsByPhoneNo(phone)) {
            Map<String, String> credentials = new HashMap<>();
            credentials.put("otp", otp);
            credentials.put("token", token);
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phone, credentials));
            String jwtToken = jwtUtil.GenerateToken((AuthUserDetail) authentication.getPrincipal());
            simpleResponse.put("success", "true");
            simpleResponse.put("token", jwtToken);
            simpleResponse.put("id", ((AuthUserDetail) authentication.getPrincipal()).getId().toString());
            return ResponseEntity.ok(simpleResponse);
        } else {
            if (otpTokenUtil.validateOTP(token, otp, phone)) {
                simpleResponse.put("message", "verification successful");
                simpleResponse.put("success", "true");

            } else {
                simpleResponse.put("message", "invalid otp");
                simpleResponse.put("success", "false");
            }
            return ResponseEntity.ok(simpleResponse);
        }

    }
    @Override
    public ResponseEntity<OtpResponse> getOtp(String phoneNumber) {
        try {
            String formattedPhoneNo = "94" + phoneNumber.trim();
            String otp = generateOtp();
            String hash = otpTokenUtil.hash(phoneNumber, otp);
            String otpMessage = "code : " + otp + '\n' + "Please use above Oh Taxi Code/OTP to complete your action" + '\n' + '\n' + "Regards" + '\n' + "Mave";
            notifyWebClient.post().uri(builder -> builder.queryParam("to", formattedPhoneNo).queryParam("message", otpMessage).build()).retrieve().bodyToMono(String.class).block();
            boolean isExistsByPhoneNumber = userRepository.existsByPhoneNo(phoneNumber);
            if (isExistsByPhoneNumber) {
                return ResponseEntity.ok(OtpResponse.builder().isOldAccount(true).token(hash).build());
            } else {
                return ResponseEntity.ok(OtpResponse.builder().isOldAccount(false).token(hash).build());
            }
        } catch (Exception e) {
            throw new OtpGenerationException(e.getMessage());
        }
    }



    public String generateOtp() {
        String characters = "0123456789";
        StringBuilder stringBuilderObj = new StringBuilder();
        Random rnd = new Random();
        while (stringBuilderObj.length() < 4) {
            int index = (int) (rnd.nextFloat() * characters.length());
            stringBuilderObj.append(characters.charAt(index));
        }
        return stringBuilderObj.toString();
    }


}
