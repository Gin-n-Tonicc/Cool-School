package com.coolSchool.coolSchool.exceptions.handlers;

import com.coolSchool.coolSchool.exceptions.token.InvalidTokenException;
import com.coolSchool.coolSchool.services.TokenService;
import com.coolSchool.coolSchool.utils.CookieHelper;
import com.coolSchool.coolSchool.utils.ObjectMapperHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.coolSchool.coolSchool.services.impl.security.TokenServiceImpl.AUTH_COOKIE_KEY_JWT;

/**
 * LogoutHandler is responsible for handling user logout by invalidating the JWT token and removing associated cookies.
 */
@Service
@RequiredArgsConstructor
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    /**
     * Performs user logout by invalidating the JWT token and removing associated cookies.
     * If the token is invalid or missing, it sends a standardized error response back to the client.
     */
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String jwt = CookieHelper.readCookie(AUTH_COOKIE_KEY_JWT, request.getCookies()).orElse(null);

        // If JWT token is missing or empty, send an error response
        if (jwt == null || jwt.isEmpty()) {
            try {
                ObjectMapperHelper.writeExceptionToObjectMapper(objectMapper, new InvalidTokenException(messageSource), response);
                return;
            } catch (IOException exception) {
                return;
            }
        }

        // Invalidate the JWT token and remove associated cookies
        tokenService.logoutToken(jwt);
        tokenService.detachAuthCookies(response::addCookie);
    }
}
