package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class TransactionException extends ApiException {

    public TransactionException(MessageSource messageSource) {
        super(messageSource.getMessage("transaction.failed", null, LocaleContextHolder.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public TransactionException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
