package com.persoff68.fatodo.security.oauth2.repository;

import com.persoff68.fatodo.security.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CookieAuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_REQUEST_COOKIE_NAME = "oauth2_request";
    public static final String OAUTH2_LANGUAGE_COOKIE_NAME = "oauth2_language";
    public static final String OAUTH2_TIMEZONE_COOKIE_NAME = "oauth2_timezone";
    public static final String OAUTH2_REDIRECT_COOKIE_NAME = "oauth2_redirect";
    private static final int COOKIE_EXPIRE_SECONDS = 180;
    public static final String LANGUAGE_REQUEST_PARAM = "language";
    public static final String TIMEZONE_REQUEST_PARAM = "timezone";
    public static final String REDIRECT_REQUEST_PARAM = "redirect";

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    public String loadLanguage(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_LANGUAGE_COOKIE_NAME).map(Cookie::getValue).orElse(null);
    }

    public String loadTimezone(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_TIMEZONE_COOKIE_NAME).map(Cookie::getValue).orElse(null);
    }

    public String loadRedirect(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_REDIRECT_COOKIE_NAME).map(Cookie::getValue).orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }

        CookieUtils.addCookie(response, OAUTH2_REQUEST_COOKIE_NAME,
                CookieUtils.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);

        String language = request.getParameter(LANGUAGE_REQUEST_PARAM);
        if (StringUtils.isNoneBlank(language)) {
            CookieUtils.addCookie(response, OAUTH2_LANGUAGE_COOKIE_NAME, language, COOKIE_EXPIRE_SECONDS);
        }

        String timezone = request.getParameter(TIMEZONE_REQUEST_PARAM);
        if (StringUtils.isNoneBlank(timezone)) {
            CookieUtils.addCookie(response, OAUTH2_TIMEZONE_COOKIE_NAME, timezone, COOKIE_EXPIRE_SECONDS);
        }

        String redirect = request.getParameter(REDIRECT_REQUEST_PARAM);
        if (StringUtils.isNoneBlank(redirect)) {
            CookieUtils.addCookie(response, OAUTH2_REDIRECT_COOKIE_NAME, redirect, COOKIE_EXPIRE_SECONDS);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, OAUTH2_LANGUAGE_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, OAUTH2_TIMEZONE_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, OAUTH2_REDIRECT_COOKIE_NAME);
    }
}
