package com.boardgaming.api.auth.repository;

import com.boardgaming.core.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class CookieAuthorizationRequestRepository implements AuthorizationRequestRepository {
    private final String oauth2AuthorizationRequest;
    private final Long cookieExpireSeconds;
    private final String cookieDomain;
    private final String sameSite;

    public CookieAuthorizationRequestRepository(
        @Value("${spring.security.oauth2.cookie-name}") final String oauth2AuthorizationRequest,
        @Value("${spring.security.oauth2.cookie-expire-seconds}") final Long cookieExpireSeconds,
        @Value("${custom.cookieDomain}") final String cookieDomain,
        @Value("${custom.sameSite}") final String sameSite
    ) {
        this.oauth2AuthorizationRequest = oauth2AuthorizationRequest;
        this.cookieExpireSeconds = cookieExpireSeconds;
        this.cookieDomain = cookieDomain;
        this.sameSite = sameSite;
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(final HttpServletRequest request) {
        return CookieUtil.getCookie(request, oauth2AuthorizationRequest)
            .map(cookie -> CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class))
            .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(
        final OAuth2AuthorizationRequest authorizationRequest,
        final HttpServletRequest request,
        final HttpServletResponse response
    ) {
        if (authorizationRequest == null) {
            CookieUtil.deleteCookie(request, response, oauth2AuthorizationRequest, cookieDomain, sameSite);
            return;
        }

        CookieUtil.addCookie(response, oauth2AuthorizationRequest, authorizationRequest, cookieExpireSeconds, cookieDomain, sameSite);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
        final HttpServletRequest request,
        final HttpServletResponse response
    ) {
        return loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(
        final HttpServletRequest request,
        final HttpServletResponse response
    ) {
        CookieUtil.deleteCookie(request, response, oauth2AuthorizationRequest, cookieDomain, sameSite);
    }
}