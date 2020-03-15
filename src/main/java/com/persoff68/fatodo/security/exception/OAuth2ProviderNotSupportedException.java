package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class OAuth2ProviderNotSupportedException extends AbstractException {
    public OAuth2ProviderNotSupportedException(String provider) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Provider " + provider + " not supported");
    }
}
