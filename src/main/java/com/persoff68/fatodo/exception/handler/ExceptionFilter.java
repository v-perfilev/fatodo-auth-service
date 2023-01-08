package com.persoff68.fatodo.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.exception.attribute.AttributeHandler;
import com.persoff68.fatodo.security.exception.OAuth2Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class ExceptionFilter extends OncePerRequestFilter {
    private static final String FEEDBACK_CODE_PARAM = "feedbackCode";

    private final ObjectMapper objectMapper;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (OAuth2Exception e) {
            String redirect = e.getRedirectUri();
            String feedbackCode = e.getFeedbackCode();
            String targetUrl = buildUrlWithFeedbackCode(redirect, feedbackCode);
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } catch (Exception e) {
            AttributeHandler.from(request, e).sendError(objectMapper, response);
        }
    }

    private String buildUrlWithFeedbackCode(String redirect, String feedbackCode) {
        return UriComponentsBuilder.fromUriString(redirect)
                .queryParam(FEEDBACK_CODE_PARAM, feedbackCode)
                .build().toUriString();
    }

}
