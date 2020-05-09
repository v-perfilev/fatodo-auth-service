package com.persoff68.fatodo.service.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UserAlreadyActivatedException extends AbstractException {
    private static final String MESSAGE = "User already activated";
    private static final String FEEDBACK_CODE = "auth.activated";

    public UserAlreadyActivatedException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE, FEEDBACK_CODE);
    }

}
