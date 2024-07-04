package com.spring.auth.service.Impl;

import com.spring.auth.dto.request.UserRequest;
import com.spring.auth.dto.response.SimpleResponse;
import com.spring.auth.dto.response.UserResponse;
import com.spring.auth.entity.AuthUserDetail;
import com.spring.auth.entity.User;
import com.spring.auth.repository.UserRepository;
import com.spring.auth.service.EmailService;
import com.spring.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    public ResponseEntity<Map<String, String>> saveUser(UserRequest userRequest) throws MessagingException {
        Map<String, String> simpleResponse = new HashMap<>();
        if (userRepository.existsByPhoneNo(userRequest.getPhoneNo())) {
            simpleResponse.put("success", "false");
            simpleResponse.put("message", "Phone number already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(simpleResponse);
        } else if (userRepository.existsByEmail(userRequest.getEmail())) {
            simpleResponse.put("success", "false");
            simpleResponse.put("message", "Email already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(simpleResponse);
        } else {
            String password = generateTemporaryPassword();
            User user = User.builder()
                    .userName(userRequest.getUserName())
                    .email(userRequest.getEmail())
                    .phoneNo(userRequest.getPhoneNo())
                    .dateOfBirth(userRequest.getDateOfBirth())
                    .gender(userRequest.getGender())
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .role(userRequest.getRole())
                    .password(passwordEncoder.encode(password))
                    .build();
            User savedUser = userRepository.save(user);
            emailService.sendDriverTemporaryPassword(user.getEmail(), password);
            AuthUserDetail userDetail = new AuthUserDetail(savedUser);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            simpleResponse.put("success", "true");
            simpleResponse.put("id", savedUser.getId().toString());
            simpleResponse.put("message", "User created successfully and logged in");
            return ResponseEntity.status(HttpStatus.CREATED).body(simpleResponse);
        }
    }

    @Transactional
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> userResponseList = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(user, userResponse);
            userResponseList.add(userResponse);
        }
        return ResponseEntity.ok(userResponseList);
    }



    private String generateTemporaryPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        Random rnd = new Random();
        while (stringBuilder.length() < 6) {
            int index = (int) (rnd.nextFloat() * characters.length());
            stringBuilder.append(characters.charAt(index));
        }
        return stringBuilder.toString();
    }

    @Transactional
    public ResponseEntity<SimpleResponse> updateUser(Long id, UserRequest userRequest) {
        try {
            Optional<User> existingUserOptional = userRepository.findById(id);
            if (existingUserOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleResponse(false, "User not found"));
            }
            User existingUser = existingUserOptional.get();

            if (!userRequest.getEmail().equals(existingUser.getEmail()) && userRepository.existsByEmailIgnoreCase(userRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new SimpleResponse(false, "Email already exists"));
            } else if (!userRequest.getPhoneNo().equals(existingUser.getPhoneNo()) &&
                    userRepository.existsByPhoneNoIgnoreCase(userRequest.getPhoneNo())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new SimpleResponse(false, "Phone number already exists"));
            }
            BeanUtils.copyProperties(userRequest, existingUser, getNullPropertyNames(userRequest));
            userRepository.save(existingUser);
            return ResponseEntity.ok(new SimpleResponse(true, "User updated successfully"));
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SimpleResponse(false, "An error occurred while updating the user"));
        }
    }

    @Transactional
    public ResponseEntity<SimpleResponse> deleteUser(Long id) {
        try {
            Optional<User> existingUserOptional = userRepository.findById(id);
            if (existingUserOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleResponse(false, "User not found"));
            }
            userRepository.deleteById(id);
            return ResponseEntity.ok(new SimpleResponse(true, "User deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SimpleResponse(false, "An error occurred while deleting the user"));
        }
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserResponse());
        }
        User user = optionalUser.get();
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        return ResponseEntity.ok(userResponse);
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames.toArray(new String[0]);
    }
}
