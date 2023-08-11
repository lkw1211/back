package com.boardgaming.api.auth.handler;

import com.boardgaming.api.auth.repository.CookieAuthorizationRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final String redirectUriFailure;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    public OAuth2FailureHandler(
        @Value("${spring.security.oauth2.redirectUriFailure}") final String redirectUriFailure,
        final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository
    ) {
        this.redirectUriFailure = redirectUriFailure;
        this.cookieAuthorizationRequestRepository = cookieAuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationFailure(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AuthenticationException authenticationException
    ) throws IOException {
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUriFailure)
            .queryParam("error", authenticationException.getLocalizedMessage())
            .build().toUriString();

        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
