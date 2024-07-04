package com.spring.auth.security;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class OtpTokenUtil {

    public  String hash(String phone,String otp) {

        long ttl = 5 * 60 * 1000;
        long expires = System.currentTimeMillis() + ttl;
        String data = phone + "." + otp + "." + expires;

        String hashedValue= createSHA256Hash(data);
        return hashedValue + "." + expires;
    }

    public static String createSHA256Hash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(data.getBytes());
            String encodeToString = Base64.getEncoder().encodeToString(encodedhash);
            return Base64.getEncoder().encodeToString(encodeToString.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  boolean validateOTP(String fullHash,String providedOTP,String phoneNo) {
        String[] parts = fullHash.split("\\.");
        if (parts.length != 2) {
            return false; // Invalid hash format
        }
        long expiryTimestamp = Long.parseLong(parts[1]);
        if (System.currentTimeMillis() > expiryTimestamp) {
            return false; // OTP has expired
        }

        String providedData=phoneNo + "." + providedOTP + "." + parts[1];
        String storedHash = parts[0];
        String computedNewHash = createSHA256Hash(providedData);
        return storedHash.equals(computedNewHash);
    }


}
