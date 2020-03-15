package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class OAuth2InternalException extends AbstractException {
    public OAuth2InternalException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Internal authentication error");
    }
}
