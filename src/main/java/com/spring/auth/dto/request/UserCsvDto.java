package com.spring.auth.dto.request;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:application.properties")
public class UserCsvDto {
    @Value("${user.id}")
    private String id;
    @Value("${user.userName}")
    private String userName;
    @Value("${user.email}")
    private String email;
    @Value("${user.phoneNo}")
    private String phoneNo;
    @Value("${user.dateOfBirth}")
    private String dateOfBirth;
    @Value("${user.gender}")
    private String gender;
    @Value("${user.role}")
    private String role;

    public String[] getHeaders() {
        String[] headers = {this.id, this.userName,
                this.email, this.phoneNo,
                this.dateOfBirth,
                this.gender, this.role};
        return headers;
    }
}
