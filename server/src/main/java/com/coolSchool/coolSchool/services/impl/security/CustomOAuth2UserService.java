package com.coolSchool.coolSchool.services.impl.security;


import com.coolSchool.coolSchool.security.CustomOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of OAuth2UserService that loads the OAuth2 user and wraps it into a CustomOAuth2User object.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Extract the registration ID from the user request
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // Load the OAuth2 user using the default implementation
        OAuth2User user = super.loadUser(userRequest);
        return new CustomOAuth2User(user, registrationId);
    }
}
