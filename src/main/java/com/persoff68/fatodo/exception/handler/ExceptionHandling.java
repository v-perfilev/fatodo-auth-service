package com.persoff68.fatodo.exception.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandling {

    private final ExceptionTranslator exceptionTranslator;

}
