package com.spring.auth.service.Impl;


import com.spring.auth.dto.request.AdminAuthRequest;
import com.spring.auth.dto.request.ChangePasswordDto;
import com.spring.auth.dto.request.UserPasswordResetRequest;
import com.spring.auth.dto.response.AuthResponse;
import com.spring.auth.dto.response.SimpleResponse;
import com.spring.auth.entity.AuthUserDetail;
import com.spring.auth.entity.User;
import com.spring.auth.entity.VerificationToken;
import com.spring.auth.repository.UserRepository;
import com.spring.auth.repository.VerificationTokenRepository;
import com.spring.auth.security.JwtUtil;
import com.spring.auth.service.EmailLoginService;
import com.spring.auth.service.EmailService;
import com.spring.auth.util.PropertyFileValue;
import com.spring.auth.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailLoginServiceImpl implements EmailLoginService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final PropertyFileValue propertyFileValue;
    private final EmailService emailService;

    @Override
    public ResponseEntity<SimpleResponse> adminLogin(AdminAuthRequest adminAuthRequest) {
        User existingUser = userRepository.findByEmail(adminAuthRequest.getEmail());
        if (existingUser == null) {
            return ResponseEntity.ok(SimpleResponse.builder().success(false).message("email not exists").build());
        } else {
            if (BCrypt.checkpw(adminAuthRequest.getPassword(), existingUser.getPassword())) {
                AuthUserDetail userDetail = new AuthUserDetail(existingUser);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtUtil.GenerateToken(userDetail);
                String email = userDetail.getEmail();
                assert existingUser.getRole() != null;
                return ResponseEntity.ok(AuthResponse.builder().success(true).message("Login Succeed")
                        .userId(userDetail.getId())
                        .userName(existingUser.getUserName())
                        .email(email)
                        .role(existingUser.getRole())
                        .token(token).build());
            } else {
                return ResponseEntity.ok(SimpleResponse.builder().success(false).message("bad credentials").build());
            }
        }

    }

    @Override
    public ResponseEntity<SimpleResponse> forgotPassword(String email, HttpServletRequest request) throws MessagingException {
        SimpleResponse simpleResponse = new SimpleResponse(false, "");
        if (!isMailExists(email)) {
            simpleResponse.setMessage("User not exists");
        } else {
            User user = userRepository.findByEmail(email);
            VerificationToken tokeVerificationToken = new VerificationToken();
            tokeVerificationToken.setUser(user);
            tokeVerificationToken.setToken(generateOtp());
            int expiryTimeInMinutes = Integer.valueOf(propertyFileValue.getVerifyTokenExpire());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            Timestamp timestamp = new Timestamp(calendar.getTimeInMillis() + expiryTimeInMinutes);
            tokeVerificationToken.setExpiryDate(timestamp);
            verificationTokenRepository.save(tokeVerificationToken);
            emailService.forgotMail(user.getEmail(), tokeVerificationToken.getToken());
            simpleResponse.setSuccess(true);
            simpleResponse.setMessage("User forgot mail send successfully");
        }
        return ResponseEntity.ok(simpleResponse);
    }

    @Override
    public ResponseEntity<SimpleResponse> checkValidationForForgotPasswordOtp(String otp) {
        SimpleResponse simpleResponse = new SimpleResponse(false, "");
        VerificationToken verificationToken = getUserByToken(otp);
        if (isTokenExist(otp)) {
            Timestamp localTime = Utils.timeFormat();
            if (verificationToken.getExpiryDate().after(localTime)) {
                simpleResponse.setSuccess(true);
                simpleResponse.setMessage("Otp verification success");
            } else {
                simpleResponse.setMessage("OTP expired");
            }
        } else {
            simpleResponse.setMessage("Invalid OTP");
        }
        return ResponseEntity.ok(simpleResponse);
    }

    @Override
    public ResponseEntity<SimpleResponse> resetUserPassword(UserPasswordResetRequest userPasswordResetRequest) throws MessagingException {
        SimpleResponse simpleResponse = new SimpleResponse(false, "");
        VerificationToken verificationToken = getUserByToken(userPasswordResetRequest.getOtp());
        if (isTokenExist(userPasswordResetRequest.getOtp())) {
            User user = userRepository.getById(verificationToken.getUser().getId());
            String encryptedPassword = passwordEncoder.encode(userPasswordResetRequest.getPassword());
            user.setPassword(encryptedPassword);
            userRepository.save(user);
            emailService.passwordResetSuccess(user.getEmail());
            simpleResponse.setSuccess(true);
            simpleResponse.setMessage("User password reset successfully");
        } else {
            simpleResponse.setMessage("Invalid OTP");
        }
        return ResponseEntity.ok(simpleResponse);
    }

    @Override
    public ResponseEntity<SimpleResponse> changePassword(ChangePasswordDto changePasswordDto, Principal principal) {
        SimpleResponse simpleResponse = new SimpleResponse(false, "");

        // Retrieve the user by their email (username)
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            simpleResponse.setMessage("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(simpleResponse);
        }

        // Retrieve the stored password hash
        String storedPasswordHash = user.getPassword();

        // Check if the provided old password matches the stored password hash
        if (passwordEncoder.matches(changePasswordDto.getOldPassword(), storedPasswordHash)) {
            // Encode the new password
            String newEncodedPassword = passwordEncoder.encode(changePasswordDto.getNewPassword());

            // Update the user's password
            user.setPassword(newEncodedPassword);
            userRepository.save(user);

            simpleResponse.setSuccess(true);
            simpleResponse.setMessage("Password changed successfully");
        } else {
            simpleResponse.setMessage("Password mismatched");
        }

        return ResponseEntity.ok(simpleResponse);
    }


    @Override
    public String getPassword(String email) {
        return userRepository.findByEmail(email).getPassword();
    }

    @Override
    public boolean isMailExists(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
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

    @Override
    public VerificationToken getUserByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public boolean isTokenExist(String token) {
        return verificationTokenRepository.existsByToken(token);
    }
}
