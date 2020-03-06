package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.constant.ExceptionTypes;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class OAuth2WrongProviderProblem extends AbstractThrowableProblem {
    public OAuth2WrongProviderProblem(String provider) {
        super(ExceptionTypes.AUTH_TYPE, "User signed up with " + provider, Status.BAD_REQUEST);
    }
}
