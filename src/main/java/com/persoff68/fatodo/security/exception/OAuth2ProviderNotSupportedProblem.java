package com.persoff68.fatodo.security.exception;

import com.persoff68.fatodo.exception.constant.ExceptionTypes;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class OAuth2ProviderNotSupportedProblem extends AbstractThrowableProblem {
    public OAuth2ProviderNotSupportedProblem(String provider) {
        super(ExceptionTypes.AUTH_TYPE, "Provider " + provider + " not supported", Status.BAD_REQUEST);
    }
}
