package com.persoff68.fatodo.service.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class ResetPasswordNotFoundException extends AbstractException {
    private static final String MESSAGE = "Reset password code not found";
    private static final String FEEDBACK_CODE = "auth.resetPasswordNotFound";

    public ResetPasswordNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE, FEEDBACK_CODE);
    }

}
