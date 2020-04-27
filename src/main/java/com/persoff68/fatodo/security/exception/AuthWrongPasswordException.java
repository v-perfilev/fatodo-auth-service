package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AuthWrongPasswordException extends AbstractException {
    private static final String MESSAGE = "Wrong password";
    private static final String FEEDBACK_CODE = "security.wrongPassword";

    public AuthWrongPasswordException() {
        super(HttpStatus.UNAUTHORIZED, MESSAGE, FEEDBACK_CODE);
    }
}
