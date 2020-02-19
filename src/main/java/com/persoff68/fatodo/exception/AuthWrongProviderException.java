package com.persoff68.fatodo.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class AuthWrongProviderException extends AbstractThrowableProblem {
    public AuthWrongProviderException(String provider) {
        super(null, "User signed up with " + provider, Status.BAD_REQUEST);
    }
}
