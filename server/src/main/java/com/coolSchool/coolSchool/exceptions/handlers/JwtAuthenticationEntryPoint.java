package com.coolSchool.coolSchool.exceptions.handlers;

import com.coolSchool.coolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.coolSchool.utils.ObjectMapperHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JwtAuthenticationEntryPoint class is responsible for handling authentication errors
 * that occur when attempting to access secured endpoints without proper authentication credentials.
 * It sends a standardized error response back to the client with appropriate HTTP status code and error message.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper, MessageSource messageSource) {
        this.objectMapper = objectMapper;
        this.messageSource = messageSource;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        // Serialize the error response using ObjectMapper and write it to the HttpServletResponse
        ObjectMapperHelper
                .writeExceptionToObjectMapper(
                        objectMapper,
                        new AccessDeniedException(messageSource),
                        httpServletResponse
                );
    }
}