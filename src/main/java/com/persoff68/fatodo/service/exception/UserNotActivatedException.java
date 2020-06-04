package com.persoff68.fatodo.service.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UserNotActivatedException extends AbstractException {
    private static final String MESSAGE = "User not activated";
    private static final String FEEDBACK_CODE = "auth.notActivated";

    public UserNotActivatedException() {
        super(HttpStatus.LOCKED, MESSAGE, FEEDBACK_CODE);
    }

}
