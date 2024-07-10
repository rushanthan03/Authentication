package com.spring.auth.dto.response;


import com.spring.auth.common.RestApiResponseStatus;
import lombok.Data;

@Data
public class ApiResponse {
  public static final ApiResponse OK = new ApiResponse(RestApiResponseStatus.OK);

  public ApiResponse(RestApiResponseStatus restApiResponseStatus) {
    this.status = restApiResponseStatus.getStatus();
    this.statusCode = restApiResponseStatus.getCode();
  }

  private String status;

  private Integer statusCode;

}
