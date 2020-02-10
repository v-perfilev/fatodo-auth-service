package com.persoff68.fatodo.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class OAuth2ProviderNotSupportedException extends AbstractThrowableProblem {
    public OAuth2ProviderNotSupportedException(String provider) {
        super(null, "Provider " + provider + " not supported", Status.BAD_REQUEST);
    }
}
