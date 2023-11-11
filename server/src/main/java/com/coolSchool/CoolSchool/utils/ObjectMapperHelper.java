package com.coolSchool.CoolSchool.utils;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import com.coolSchool.CoolSchool.models.dto.response.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;

public class ObjectMapperHelper {
    public static void writeExceptionToObjectMapper(
            ObjectMapper objectMapper,
            ApiException exception,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        ExceptionResponse exceptionResponse = ApiExceptionParser.parseException(exception);
        httpServletResponse.setStatus(exceptionResponse.getStatusCode());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ServletOutputStream out = httpServletResponse.getOutputStream();
        objectMapper.writeValue(out, exceptionResponse);
    }
}
