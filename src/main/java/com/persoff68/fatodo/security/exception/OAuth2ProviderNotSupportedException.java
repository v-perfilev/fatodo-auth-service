package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class OAuth2ProviderNotSupportedException extends AbstractException {
    private static final String MESSAGE_WITH_PARAM = " OAuth2 provider not supported";
    private static final String FEEDBACK_CODE = "security.oauth2.provider-not-supported";

    public OAuth2ProviderNotSupportedException(String provider) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, provider + MESSAGE_WITH_PARAM, FEEDBACK_CODE);
    }
}
