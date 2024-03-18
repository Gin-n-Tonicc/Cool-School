package com.coolSchool.coolSchool.services.impl.security;

import com.coolSchool.coolSchool.config.FrontendConfig;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.services.UserService;
import com.coolSchool.coolSchool.services.impl.security.events.OnPasswordResetRequestEvent;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Component responsible for listening to password reset request events and sending password reset emails.
 */
@Component
@RequiredArgsConstructor
public class PasswordResetListener implements ApplicationListener<OnPasswordResetRequestEvent> {

    private final JavaMailSender mailSender;
    private final FrontendConfig frontendConfig;
    private final UserService service;

    /**
     * Sends a password reset email upon receiving the password reset request event.
     */
    @Override
    public void onApplicationEvent(OnPasswordResetRequestEvent event) {
        sendPasswordResetEmail(event);
    }

    private void sendPasswordResetEmail(OnPasswordResetRequestEvent event) {
        User user = event.getUser();

        // Generate a password reset token and create a verification token for the user
        String token = generateResetToken(user);
        service.createVerificationToken(user, token);
        String recipientAddress = user.getEmail();
        String subject = "Password Reset Request";
        String message = getEmailMessage(token, user);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }

    @NotNull
    private String getEmailMessage(String token, User user) {
        //Constructs the email message for the password reset request.
        String confirmationUrl = frontendConfig.getForgottenPasswordUrl() + "?token=" + token;

        return "Dear " + user.getFirstname() + ",\n\n"
                + "A password reset request has been initiated for your account.\n"
                + "Please click the following link to reset your password:\n"
                + confirmationUrl + "\n\n"
                + "If you did not request this change, please ignore this email.\n\n"
                + "Best regards,\n"
                + "YourApp Team";
    }

    private String generateResetToken(User user) {
        return UUID.randomUUID().toString();
    }
}