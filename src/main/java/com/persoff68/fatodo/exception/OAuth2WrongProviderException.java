package com.persoff68.fatodo.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class OAuth2WrongProviderException extends AbstractThrowableProblem {
    public OAuth2WrongProviderException(String provider) {
        super(null, "User signed up with " + provider, Status.BAD_REQUEST);
    }
}
