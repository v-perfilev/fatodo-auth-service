package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AuthWrongProviderException extends AbstractException {
    private static final String MESSAGE_WITH_PARAM = "Wrong provider ";
    private static final String FEEDBACK_CODE_WITH_PARAM = "security.wrongProvider.";

    public AuthWrongProviderException(String provider) {
        super(HttpStatus.BAD_REQUEST, MESSAGE_WITH_PARAM + provider, FEEDBACK_CODE_WITH_PARAM + provider);
    }
}
