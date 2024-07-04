package com.spring.auth.service;

import javax.mail.MessagingException;

public interface EmailService {

    void forgotMail(String mail, String token) throws MessagingException;

    void passwordResetSuccess(String email) throws MessagingException;

    void sendDriverTemporaryPassword(String email, String password) throws MessagingException;

}
