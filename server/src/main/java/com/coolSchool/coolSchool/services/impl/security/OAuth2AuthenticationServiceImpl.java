package com.coolSchool.coolSchool.services.impl.security;

import com.coolSchool.coolSchool.models.dto.auth.AuthenticationResponse;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.security.CustomOAuth2User;
import com.coolSchool.coolSchool.services.OAuth2AuthenticationService;
import com.coolSchool.coolSchool.services.TokenService;
import com.coolSchool.coolSchool.services.UserService;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@AllArgsConstructor
public class OAuth2AuthenticationServiceImpl implements OAuth2AuthenticationService {
    private final UserService userService;
    private final TokenService tokenService;

    @Override
    public boolean processOAuthPostLogin(CustomOAuth2User oAuth2User, Consumer<Cookie> addCookieFunc) {
        User user = userService.processOAuthUser(oAuth2User);

        tokenService.revokeAllUserTokens(user);
        AuthenticationResponse authResponse = tokenService.generateAuthResponse(user);
        tokenService.attachAuthCookies(authResponse, addCookieFunc);

        return user.isAdditionalInfoRequired();
    }
}
