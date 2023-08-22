package com.coolSchool.CoolSchool.exceptions.handlers;

import com.coolSchool.CoolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.CoolSchool.models.dto.ExceptionResponse;
import com.coolSchool.CoolSchool.utils.ApiExceptionParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        ExceptionResponse exceptionResponse = ApiExceptionParser.parseException(new AccessDeniedException());
        httpServletResponse.setStatus(exceptionResponse.getStatusCode());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ServletOutputStream out = httpServletResponse.getOutputStream();
        objectMapper.writeValue(out, exceptionResponse);
    }
}