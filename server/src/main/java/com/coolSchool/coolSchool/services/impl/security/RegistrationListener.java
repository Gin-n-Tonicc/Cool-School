package com.coolSchool.coolSchool.services.impl.security;

import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.services.UserService;
import com.coolSchool.coolSchool.services.impl.security.events.OnRegistrationCompleteEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final UserService service;

    private final MessageSource messages;

    private final JavaMailSender mailSender;

    @Value("${server.app.base-url}")
    private String appBaseUrl;

    public RegistrationListener(UserService service, MessageSource messages, JavaMailSender mailSender) {
        this.service = service;
        this.messages = messages;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(@NotNull OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Cool School Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "auth/registrationConfirm?token=" + token;

        String message = "Dear, " + user.getFirstname() + "\n\n"
                + "Thank you for registering with Cool School!\n\n"
                + "To complete your registration, please click the following link to verify your email:\n"
                + confirmationUrl + "\n"
                + "If you did not create an account with us, please ignore this email.\n"
                + "Best regards,\n"
                + "Cool School Team!";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }
}
