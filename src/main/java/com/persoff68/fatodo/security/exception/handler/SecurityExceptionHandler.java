package com.persoff68.fatodo.security.exception.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.exception.attribute.AttributeHandler;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 30)
@RequiredArgsConstructor
public class SecurityExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleAbstractException(HttpServletRequest request)
            throws IOException {
        Exception e = new UnauthorizedException();
        return AttributeHandler.from(request, e).getResponseEntity(objectMapper);
    }

}