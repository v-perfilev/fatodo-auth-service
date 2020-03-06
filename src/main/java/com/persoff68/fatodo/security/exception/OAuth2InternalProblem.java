package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.constant.ExceptionTypes;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class OAuth2InternalProblem extends AbstractThrowableProblem {
    public OAuth2InternalProblem() {
        super(ExceptionTypes.AUTH_TYPE, "Internal authentication error", Status.INTERNAL_SERVER_ERROR);
    }
}
