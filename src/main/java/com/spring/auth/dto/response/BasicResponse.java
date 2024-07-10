package com.spring.auth.dto.response;


import com.spring.auth.common.RestApiResponseStatus;

public class BasicResponse<T> extends ApiResponse {
  private String message;

  public BasicResponse(RestApiResponseStatus restApiResponseStatus) {
    super(restApiResponseStatus);
  }

  public BasicResponse() {
    super(RestApiResponseStatus.OK);
  }

  public BasicResponse(RestApiResponseStatus restApiResponseStatus, String message) {
    super(restApiResponseStatus);
    this.setMessage(message);
  }

  public BasicResponse(T responseBody, RestApiResponseStatus restApiResponseStatus,
      String message) {
    super(restApiResponseStatus);
    this.setMessage(message);
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
