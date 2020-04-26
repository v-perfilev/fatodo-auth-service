package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class OAuth2InternalException extends AbstractException {
    private static final String MESSAGE = "OAuth2 error";
    private static final String FEEDBACK_CODE = "security.oauth2.internal";

    public OAuth2InternalException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE, FEEDBACK_CODE);
    }
}
