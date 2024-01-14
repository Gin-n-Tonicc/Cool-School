package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.auth.*;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.security.CustomOAuth2User;
import jakarta.servlet.http.Cookie;

import java.io.IOException;
import java.util.function.Consumer;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(RefreshTokenBodyDTO refreshTokenBodyDTO) throws IOException;

    AuthenticationResponse me(
            AccessTokenBodyDTO accessTokenBodyDTO
    );

    void attachAuthCookies(AuthenticationResponse authenticationResponse, Consumer<Cookie> cookieConsumer);
}
