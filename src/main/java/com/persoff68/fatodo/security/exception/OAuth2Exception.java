package com.persoff68.fatodo.security.exception;

import lombok.Getter;

public class OAuth2Exception extends RuntimeException {
    @Getter
    private String redirectUri;
    @Getter
    private String feedbackCode;

    public OAuth2Exception(String message, String redirectUri, String feedbackCode) {
        super(message);
        this.redirectUri = redirectUri;
        this.feedbackCode = feedbackCode;
    }
}
