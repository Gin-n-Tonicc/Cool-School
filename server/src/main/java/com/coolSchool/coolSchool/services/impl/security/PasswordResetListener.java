package com.coolSchool.coolSchool.services.impl.security;

import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.services.UserService;
import com.coolSchool.coolSchool.services.impl.security.events.OnPasswordResetRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PasswordResetListener implements ApplicationListener<OnPasswordResetRequestEvent> {

    private final JavaMailSender mailSender;
    @Autowired
    private UserService service;

    @Override
    public void onApplicationEvent(OnPasswordResetRequestEvent event) {
        sendPasswordResetEmail(event);
    }

    private void sendPasswordResetEmail(OnPasswordResetRequestEvent event) {
        User user = event.getUser();
        String appBaseUrl = event.getAppBaseUrl();
        String token = generateResetToken(user);
        service.createVerificationToken(user, token);
        String recipientAddress = user.getEmail();
        String subject = "Password Reset Request";
        String confirmationUrl = appBaseUrl + "auth/password-reset?token=" + token;

        String message = "Dear " + user.getFirstname() + ",\n\n"
                + "A password reset request has been initiated for your account.\n"
                + "Please click the following link to reset your password:\n"
                + confirmationUrl + "\n\n"
                + "If you did not request this change, please ignore this email.\n\n"
                + "Best regards,\n"
                + "YourApp Team";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }

    private String generateResetToken(User user) {
        return UUID.randomUUID().toString();
    }
}