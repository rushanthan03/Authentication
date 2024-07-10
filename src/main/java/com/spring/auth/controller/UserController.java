package com.spring.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.auth.common.RestApiResponseStatus;
import com.spring.auth.dto.request.UserRequest;
import com.spring.auth.dto.response.BasicResponse;
import com.spring.auth.dto.response.SimpleResponse;
import com.spring.auth.dto.response.UserResponse;
import com.spring.auth.service.UserService;
import com.spring.auth.util.Constants;
import com.spring.auth.util.EndpointURI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin("*")
@RestController
@SecurityRequirement(name = "user-service")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Save a new user")
    @PostMapping(value = EndpointURI.USER)
    public ResponseEntity<Map<String, String>> saveUser(@Valid @RequestBody UserRequest userRequest) throws MessagingException {
        try {
            return userService.saveUser(userRequest);
        } catch (MessagingException e) {
            log.error("Error while saving user: {}", e.getMessage(), e);
            throw e; // Consider handling it in a global exception handler
        }
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

    @Operation(summary = "Download user details in CSV")
    @GetMapping(value = EndpointURI.CSV_DOWNLOAD)
    public ResponseEntity<Resource> getFile() {
        String filename = "Employee.csv";
        InputStreamResource file = new InputStreamResource(userService.load());
        log.info("CSV Download Success");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv")).body(file);
    }

    @Operation(summary = "Upload bulk user details in CSV")
    @PostMapping(value = EndpointURI.CSV_UPLOAD)
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (userService.hasCSVFormat(file)) {
            try {
                message = file.getOriginalFilename();
                log.info("Employee CSV/Excel Import Success");
                return userService.saveCSV(file);
            } catch (Exception e) {
                message = file.getOriginalFilename();
                log.error("File Type not Excel/CSV: {}", e.getMessage(), e);
                return new ResponseEntity<>(
                        new BasicResponse<>(RestApiResponseStatus.ERROR, Constants.BULK_EMPLOYEES_FAILD + message),
                        HttpStatus.BAD_REQUEST);
            }
        }
        log.info("Employee CSV/Excel Import Failed");
        return new ResponseEntity<>(
                new BasicResponse<>(RestApiResponseStatus.ERROR, Constants.BULK_EMPLOYEES_ERROR + message),
                HttpStatus.BAD_REQUEST);
    }
}
