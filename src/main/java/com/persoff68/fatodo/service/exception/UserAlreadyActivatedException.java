package com.persoff68.fatodo.service.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UserAlreadyActivatedException extends AbstractException {
    private static final String MESSAGE = "User already activated";
    private static final String FEEDBACK_CODE = "auth.alreadyActivated";

    public UserAlreadyActivatedException() {
        super(HttpStatus.CONFLICT, MESSAGE, FEEDBACK_CODE);
    }

}
