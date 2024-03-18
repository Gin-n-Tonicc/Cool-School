package com.coolSchool.coolSchool.services.impl.security.events;

import com.coolSchool.coolSchool.models.entity.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

/**
 * OnRegistrationCompleteEvent represents an event raised when a user completes registration.
 * It carries information about the registered user, the application URL, and the locale.
 */
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private User user;

    public OnRegistrationCompleteEvent(
            User user, String appUrl) {
        super(user);

        this.user = user;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
