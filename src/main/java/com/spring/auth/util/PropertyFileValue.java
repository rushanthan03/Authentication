package com.spring.auth.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@PropertySource("classpath:application.properties")
public final class PropertyFileValue {

  private PropertyFileValue() {
  }

  // Forgot password otp expiry time
  @Value("${user.verify.token.expiray.time}")
  private String verifyTokenExpire;
}
