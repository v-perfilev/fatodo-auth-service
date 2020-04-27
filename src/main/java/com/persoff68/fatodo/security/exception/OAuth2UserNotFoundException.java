package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class OAuth2UserNotFoundException extends AbstractException {
    private static final String MESSAGE = " OAuth2 user not registered";
    private static final String FEEDBACK_CODE = "security.oauth2.notRegistered";

    public OAuth2UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE, FEEDBACK_CODE);
    }
}
