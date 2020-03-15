package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class OAuth2EmailNotFoundException extends AbstractException {
    public OAuth2EmailNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Email not found in OAuth2 provider data");
    }
}
