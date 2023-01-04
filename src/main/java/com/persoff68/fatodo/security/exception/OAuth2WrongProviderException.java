package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class OAuth2WrongProviderException extends AbstractException {
    private static final String MESSAGE_WITH_PARAM = "Wrong provider, try to login directly with your email";
    private static final String FEEDBACK_CODE = "security.oauth2.wrongProvider";

    public OAuth2WrongProviderException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE_WITH_PARAM, FEEDBACK_CODE);
    }
}
