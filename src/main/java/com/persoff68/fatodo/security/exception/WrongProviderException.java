package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class WrongProviderException extends AbstractException {
    public WrongProviderException(String provider) {
        super(HttpStatus.BAD_REQUEST, "User signed up with " + provider);
    }
}
