package com.persoff68.fatodo.security.exception.handler;


import com.persoff68.fatodo.security.exception.OAuth2Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 30)
@RequiredArgsConstructor
public class SecurityExceptionHandler {

    @ExceptionHandler(OAuth2Exception.class)
    public RedirectView handleOAuth2Exception(OAuth2Exception e) {
        String redirectUri = e.getRedirectUri();
        String feedbackCode = e.getFeedbackCode();
        RedirectView redirectView = new RedirectView(redirectUri);
        return redirectView;
    }

}
