package com.spring.auth.dto.response;

import lombok.Data;

import java.util.Set;
@Data
public class ImportErrorResponseDto {
  Set<String> employeesId;
  Set<String> employeesEmail;
}
