package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AuthUserNotFoundException extends AbstractException {
    private static final String MESSAGE = "User not registered";
    private static final String FEEDBACK_CODE = "security.notRegistered";

    public AuthUserNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE, FEEDBACK_CODE);
    }
}
