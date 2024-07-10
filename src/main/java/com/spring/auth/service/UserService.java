package com.spring.auth.service;


import com.spring.auth.dto.request.UserRequest;
import com.spring.auth.dto.response.SimpleResponse;
import com.spring.auth.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<Map<String, String>> saveUser(UserRequest userRequest) throws MessagingException;

    public ResponseEntity<List<UserResponse>> getAllUsers();

    ResponseEntity<SimpleResponse> updateUser(Long id, UserRequest userRequest);

    ResponseEntity<SimpleResponse> deleteUser(Long id);

    ResponseEntity<UserResponse> getUserById(Long id);

    public ResponseEntity<Object> saveCSV(MultipartFile file) throws MessagingException;

    public ByteArrayInputStream load();

    boolean hasCSVFormat(MultipartFile file);
}
