package com.spring.auth.service.Impl;

import com.spring.auth.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @Async
    @Transactional
    public void forgotMail(String mail, String token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(mail);
        helper.setSubject("Forgot Password");

        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("otp", token);

        Context context = new Context();
        context.setVariables(templateVariables);

        String userCredentialsTemplate = templateEngine.process("forgot-password-otp.html", context);
        helper.setText(userCredentialsTemplate, true);
        javaMailSender.send(message);
    }

    @Async
    @Transactional
    public void passwordResetSuccess(String email) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        helper.setSubject("Reset Password");

        Context context = new Context();

        String userCredentialsTemplate = templateEngine.process("user-password-reset.html", context);
        helper.setText(userCredentialsTemplate, true);
        javaMailSender.send(message);
    }

    @Async
    public void sendDriverTemporaryPassword(String email, String password) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        helper.setSubject("Temporary Password");

        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("password", password);

        Context context = new Context();
        context.setVariables(templateVariables);

        String userCredentialsTemplate = templateEngine.process("driver-temporary-password.html", context);
        helper.setText(userCredentialsTemplate, true);
        javaMailSender.send(message);

    }

}
