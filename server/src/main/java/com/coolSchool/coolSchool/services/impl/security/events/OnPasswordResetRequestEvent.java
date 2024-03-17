package com.coolSchool.coolSchool.services.impl.security.events;

import com.coolSchool.coolSchool.models.entity.User;
import org.springframework.context.ApplicationEvent;

/**
 * OnPasswordResetRequestEvent represents an event raised when a password reset request is initiated.
 * It carries information about the user requesting the password reset and the base URL of the application.
 */
public class OnPasswordResetRequestEvent extends ApplicationEvent {

    private final User user;
    private final String appBaseUrl;

    public OnPasswordResetRequestEvent(User user, String appBaseUrl) {
        super(user);
        this.user = user;
        this.appBaseUrl = appBaseUrl;
    }

    public User getUser() {
        return user;
    }

    public String getAppBaseUrl() {
        return appBaseUrl;
    }
}
