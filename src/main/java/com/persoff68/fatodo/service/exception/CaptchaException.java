package com.persoff68.fatodo.service.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public final class CaptchaException extends AbstractException {
    private static final String MESSAGE = "Robot test failed";
    private static final String FEEDBACK_CODE = "auth.captchaCheckFailed";

    public CaptchaException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE, FEEDBACK_CODE);
    }

}
