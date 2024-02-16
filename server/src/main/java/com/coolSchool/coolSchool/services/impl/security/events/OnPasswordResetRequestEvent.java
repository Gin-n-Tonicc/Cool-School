package com.coolSchool.coolSchool.services.impl.security.events;

import com.coolSchool.coolSchool.models.entity.User;
import org.springframework.context.ApplicationEvent;

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
