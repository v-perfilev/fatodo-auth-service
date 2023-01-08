package com.persoff68.fatodo.security.exception;

public class OAuth2WrongProviderException extends OAuth2Exception {
    private static final String MESSAGE = "Wrong provider, user registered local";
    private static final String FEEDBACK_CODE = "security.oauth2.wrongProvider";

    public OAuth2WrongProviderException(String redirectUri) {
        super(MESSAGE, redirectUri, FEEDBACK_CODE);
    }
}
