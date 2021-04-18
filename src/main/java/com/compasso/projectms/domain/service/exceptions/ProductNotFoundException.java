package com.compasso.projectms.domain.service.exceptions;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super (message);
    }
}
