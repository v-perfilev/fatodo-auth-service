package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.constant.ExceptionTypes;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class OAuth2EmailNotFoundProblem extends AbstractThrowableProblem {
    public OAuth2EmailNotFoundProblem() {
        super(ExceptionTypes.AUTH_TYPE, "Email not found in OAuth2 provider data", Status.NOT_FOUND);
    }
}
