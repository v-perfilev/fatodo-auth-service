package com.persoff68.fatodo.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class OAuth2InternalException extends AbstractThrowableProblem {
    public OAuth2InternalException() {
        super(null, "Internal authentication error", Status.INTERNAL_SERVER_ERROR);
    }
}
