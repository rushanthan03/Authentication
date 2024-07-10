package com.spring.auth.service.Impl;

import com.spring.auth.common.RestApiResponseStatus;
import com.spring.auth.common.RoleType;
import com.spring.auth.dto.request.UserCsvDto;
import com.spring.auth.dto.request.UserRequest;
import com.spring.auth.dto.response.*;
import com.spring.auth.entity.AuthUserDetail;
import com.spring.auth.entity.User;
import com.spring.auth.repository.UserRepository;
import com.spring.auth.service.EmailService;
import com.spring.auth.service.UserService;
import com.spring.auth.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.*;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserCsvDto userCsvDto;

    String password = generateTemporaryPassword();

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

    @Override
    public ResponseEntity<Object> saveCSV(MultipartFile file) throws MessagingException {
        try {
            return converting(importEmployeeDetails(file.getInputStream()), file.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
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

    private ResponseEntity<Object> converting(Iterable<CSVRecord> csvRecords, String filename) throws MessagingException {
        List<String> employeesId = new ArrayList<String>();
        List<String> employeesEmail = new ArrayList<String>();
        Set<String> employeesIdSet = new HashSet<String>();
        Set<String> employeesEmailSet = new HashSet<String>();
        for (CSVRecord csvRecord : csvRecords) {
            User user = new User();
            if (userRepository.existsById(Long.valueOf(csvRecord.get(userCsvDto.getId())))) {
                employeesId.add((csvRecord.get(userCsvDto.getId())));
                log.info("Bulk>> Employee Id AlreadyExists:{}", csvRecord.get(userCsvDto.getId()));
                continue;
            } else {
                user.setId(Long.valueOf(csvRecord.get(userCsvDto.getId())));
            }
            user.setUserName(csvRecord.get(userCsvDto.getUserName()));
            user.setEmail(csvRecord.get(userCsvDto.getEmail()));
            user.setPhoneNo(csvRecord.get(userCsvDto.getPhoneNo()));
            user.setDateOfBirth(csvRecord.get(userCsvDto.getDateOfBirth()));
            user.setGender(csvRecord.get(userCsvDto.getGender()));
            user.setRole(RoleType.valueOf(csvRecord.get(userCsvDto.getRole())));
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setPassword(passwordEncoder.encode(password));

            if (userRepository.existsByEmail(csvRecord.get(userCsvDto.getEmail()))) {
                employeesEmail.add((csvRecord.get(userCsvDto.getId())));
                log.info("Bulk>>Employee Email Already Exists:{}",
                        csvRecord.get(userCsvDto.getEmail()));
                continue;
            } else {
                user.setEmail(csvRecord.get(userCsvDto.getEmail()));
            }
            if (user.getId() != null) {
                User savedUser = userRepository.save(user);
                emailService.sendDriverTemporaryPassword(user.getEmail(), password);
                AuthUserDetail userDetail = new AuthUserDetail(savedUser);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        employeesIdSet.addAll(employeesId);
        employeesEmailSet.addAll(employeesEmail);
        ImportErrorResponseDto importErrorResponseDto = new ImportErrorResponseDto();
        importErrorResponseDto.setEmployeesId(employeesIdSet);
        importErrorResponseDto.setEmployeesEmail(employeesEmailSet);
        if (employeesIdSet.isEmpty() && employeesEmailSet.isEmpty()) {
            return new ResponseEntity<>(new BasicResponse<>(RestApiResponseStatus.OK,
                    Constants.BULK_EMPLOYEES_SUCCESS + filename), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ContentResponse<>(Constants.BULK_IMPORT, importErrorResponseDto,
                RestApiResponseStatus.OK), null, HttpStatus.OK);
    }

    private Iterable<CSVRecord> importEmployeeDetails(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            return csvParser.getRecords();
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    @Override
    public ByteArrayInputStream load() {
        List<User> user = userRepository.findAll();
        ByteArrayInputStream in = exportEmployeeDetails(user);
        return in;
    }

    private ByteArrayInputStream exportEmployeeDetails(List<User> users) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            csvPrinter.printRecord((Object[]) userCsvDto.getHeaders());  // Casting to Object[]

            for (User user : users) {
                List<Object> data = Arrays.asList(
                        String.valueOf(user.getId()),
                        user.getUserName(),
                        user.getEmail(),
                        user.getPhoneNo(),
                        user.getDateOfBirth(),
                        user.getGender(),
                        user.getRole()
                );
                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to import data to CSV file: " + e.getMessage());
        }
    }


    @Override
    public boolean hasCSVFormat(MultipartFile file) {
        String TYPE = "text/csv";
        if (!TYPE.equals(file.getContentType())) {
            return true;
        }
        return true;
    }

}
