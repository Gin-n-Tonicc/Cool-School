package com.coolSchool.coolSchool.utils;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import com.coolSchool.coolSchool.models.dto.response.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * ObjectMapperHelper provides utility methods for working with ObjectMapper and handling exceptions.
 */
public class ObjectMapperHelper {
    public static void writeExceptionToObjectMapper(
            ObjectMapper objectMapper,
            ApiException exception,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        // Parse the ApiException to an ExceptionResponse
        ExceptionResponse exceptionResponse = ApiExceptionParser.parseException(exception);

        // Set the HTTP response status and content type
        httpServletResponse.setStatus(exceptionResponse.getStatusCode());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Get the output stream of the response
        ServletOutputStream out = httpServletResponse.getOutputStream();

        // Write the exception response as JSON to the output stream using the provided ObjectMapper
        objectMapper.writeValue(out, exceptionResponse);
    }
}
