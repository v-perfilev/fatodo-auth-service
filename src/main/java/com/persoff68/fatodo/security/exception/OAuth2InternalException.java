package com.persoff68.fatodo.security.exception;

public class OAuth2InternalException extends OAuth2Exception {
    private static final String MESSAGE = "OAuth2 error";
    private static final String FEEDBACK_CODE = "security.oauth2.internal";

    public OAuth2InternalException(String redirectUri) {
        super(MESSAGE, redirectUri, FEEDBACK_CODE);
    }
}
