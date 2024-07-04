package com.spring.auth.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PhoneNumberAuthenticationProvider implements AuthenticationProvider {
    private static final Logger log = LoggerFactory.getLogger(PhoneNumberAuthenticationProvider.class);
    private final UserDetailsService userDetailsService;
    private final OtpTokenUtil otpTokenUtil;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phoneNumber = (String) authentication.getPrincipal();
        Map<String, String> credentials = (Map) authentication.getCredentials();
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(phoneNumber);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        } else if (!this.otpTokenUtil.validateOTP(credentials.get("token"),credentials.get("otp"), phoneNumber)) {
            throw new AuthenticationException("invalid credentials") {
            };
        } else {
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        }
    }

    public boolean supports(Class<?> authentication) {
        return true;
    }

    public static PhoneNumberAuthenticationProviderBuilder builder() {
        return new PhoneNumberAuthenticationProviderBuilder();
    }

    public UserDetailsService getUserDetailsService() {
        return this.userDetailsService;
    }

    public OtpTokenUtil getOtpTokenUtil() {
        return this.otpTokenUtil;
    }

    public PhoneNumberAuthenticationProvider(UserDetailsService userDetailsService, OtpTokenUtil otpTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.otpTokenUtil = otpTokenUtil;
    }

    public static class PhoneNumberAuthenticationProviderBuilder {
        private UserDetailsService userDetailsService;
        private OtpTokenUtil otpTokenUtil;

        PhoneNumberAuthenticationProviderBuilder() {
        }

        public PhoneNumberAuthenticationProviderBuilder userDetailsService(UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
            return this;
        }

        public PhoneNumberAuthenticationProviderBuilder otpTokenUtil(OtpTokenUtil otpTokenUtil) {
            this.otpTokenUtil = otpTokenUtil;
            return this;
        }

        public PhoneNumberAuthenticationProvider build() {
            return new PhoneNumberAuthenticationProvider(this.userDetailsService, this.otpTokenUtil);
        }

        public String toString() {
            String var10000 = String.valueOf(this.userDetailsService);
            return "PhoneNumberAuthenticationProvider.PhoneNumberAuthenticationProviderBuilder(userDetailsService=" + var10000 + ", otpTokenUtil=" + String.valueOf(this.otpTokenUtil) + ")";
        }
    }
}
