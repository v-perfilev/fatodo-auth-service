package com.persoff68.fatodo.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class OAuth2EmailNotFoundException extends AbstractThrowableProblem {
    public OAuth2EmailNotFoundException() {
        super(null, "Email not found in OAuth2 provider data", Status.NOT_FOUND);
    }
}
