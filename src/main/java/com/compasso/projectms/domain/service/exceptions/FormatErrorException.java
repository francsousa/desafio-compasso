package com.compasso.projectms.domain.service.exceptions;

public class FormatErrorException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FormatErrorException(String message) {
        super (message);
    }
}
