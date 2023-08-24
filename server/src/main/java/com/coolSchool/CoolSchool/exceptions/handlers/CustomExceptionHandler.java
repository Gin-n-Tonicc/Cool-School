package com.coolSchool.CoolSchool.exceptions.handlers;

import com.coolSchool.CoolSchool.exceptions.common.*;
import com.coolSchool.CoolSchool.exceptions.user.UserLoginException;
import com.coolSchool.CoolSchool.models.dto.ExceptionResponse;
import com.coolSchool.CoolSchool.utils.ApiExceptionParser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeExceptions(RuntimeException exception) {
        // Log data
        return handleApiExceptions(new InternalServerErrorException());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ExceptionResponse> handleInternalAuthServiceExceptions(InternalAuthenticationServiceException exception) {
        Throwable cause = exception.getCause();

        if (cause instanceof ApiException) {
            return handleApiExceptions((ApiException) cause);
        }

        return handleRuntimeExceptions(exception);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsExceptions() {
        return handleApiExceptions(new UserLoginException());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleFileNotFoundException(FileNotFoundException exception) {
        return handleApiExceptions(new FileNotFoundException());
    }

    @ExceptionHandler(DirectoryCreationException.class)
    public ResponseEntity<ExceptionResponse> handleDirectoryCreationException(DirectoryCreationException directoryCreationException) {
        return handleApiExceptions(new DirectoryCreationException());
    }

    @ExceptionHandler(UnsupportedFileTypeException.class)
    public ResponseEntity<ExceptionResponse> handleUnsupportedFileTypeException(UnsupportedFileTypeException unsupportedFileTypeException) {
        return handleApiExceptions(new UnsupportedFileTypeException());
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedExceptions() {
        return handleApiExceptions(new AccessDeniedException());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ExceptionResponse> handleApiExceptions(ApiException exception) {
        ExceptionResponse apiException = ApiExceptionParser.parseException(exception);

        return ResponseEntity
                .status(apiException.getStatus())
                .body(apiException);
    }
}
