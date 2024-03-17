package com.coolSchool.coolSchool.security;


import com.coolSchool.coolSchool.enums.Provider;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * CustomOAuth2User class represents a custom implementation of the OAuth2User interface.
 * It wraps an existing OAuth2User and provides additional functionality to retrieve user attributes.
 */
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauth2User;

    @Getter
    private final Provider provider;

    public CustomOAuth2User(OAuth2User oauth2User, String registrationId) {
        this.oauth2User = oauth2User;
        this.provider = Provider.getProvider(registrationId);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getEmail() {
        return oauth2User.getAttribute("email");
    }
}

