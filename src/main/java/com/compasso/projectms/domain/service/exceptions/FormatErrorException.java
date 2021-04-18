package com.compasso.projectms.domain.service.exceptions;

public class FormatErrorException extends RuntimeException {

    public FormatErrorException(String message) {
        super (message);
    }
}
