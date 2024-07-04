package com.spring.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.auth.dto.request.UserRequest;
import com.spring.auth.dto.response.SimpleResponse;
import com.spring.auth.dto.response.UserResponse;
import com.spring.auth.service.UserService;
import com.spring.auth.util.EndpointURI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@SecurityRequirement(name = "user-service")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "Save a new user")
    @PostMapping(value = EndpointURI.USER)
    public ResponseEntity<Map<String, String>> saveUser(@Valid @RequestBody UserRequest userRequest) throws MessagingException {
        return userService.saveUser(userRequest);
    }

    @Operation(summary = "Get all users")
    @GetMapping(value = EndpointURI.USERS)
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Update an existing user")
    @PutMapping(value = EndpointURI.USER_UPDATE)
    public ResponseEntity<SimpleResponse> updateUser(
            @PathVariable("id") Long id, @Valid @RequestBody UserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }

    @Operation(summary = "Delete a user by ID")
    @DeleteMapping(value = EndpointURI.USER_BY_ID)
    public ResponseEntity<SimpleResponse> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @Operation(summary = "Get a user by ID")
    @GetMapping(value = EndpointURI.GET_USER_BY_ID)
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }
}
