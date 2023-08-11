package com.boardgaming.api.auth.handler;

import com.boardgaming.api.application.auth.command.SignService;
import com.boardgaming.api.auth.repository.CookieAuthorizationRequestRepository;
import com.boardgaming.domain.auth.domain.OAuth2Attributes;
import com.boardgaming.domain.auth.domain.OAuth2TempAttributes;
import com.boardgaming.domain.auth.domain.repository.OAuth2TempAttributesRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final String redirectUriSuccess;
    private final String redirectUriSignUp;
    private final SignService signService;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2TempAttributesRepository oAuth2TempAttributesRepository;

    public OAuth2SuccessHandler(
        @Value("${spring.security.oauth2.redirectUriSuccess}") final String redirectUriSuccess,
        @Value("${spring.security.oauth2.redirectUriSignUp}") String redirectUriSignUp,
        final SignService signService,
        final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository,
        final OAuth2TempAttributesRepository oAuth2TempAttributesRepository
    ) {
        this.redirectUriSuccess = redirectUriSuccess;
        this.redirectUriSignUp = redirectUriSignUp;
        this.signService = signService;
        this.cookieAuthorizationRequestRepository = cookieAuthorizationRequestRepository;
        this.oAuth2TempAttributesRepository = oAuth2TempAttributesRepository;
    }

    @Override
    public void onAuthenticationSuccess(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication
    ) throws IOException {
        String targetUrl = redirectUriSuccess;

        OAuth2Attributes attributes = (OAuth2Attributes) (((OAuth2User) authentication.getPrincipal())
            .getAttributes()
            .get("oAuth2Attributes"));

        if (Objects.nonNull(attributes)) {
            attributes.getAttributes().remove("oAuth2Attributes");
            OAuth2TempAttributes tempAttributes =
                oAuth2TempAttributesRepository.save(OAuth2TempAttributes.fromOAuth2Attributes(attributes));
            targetUrl = redirectUriSignUp + tempAttributes.getKey();
        } else {
            signService.signIn(response, authentication);
        }

        if (response.isCommitted()) {
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(
            request,
            response,
            targetUrl
        );
    }

    protected void clearAuthenticationAttributes(
        final HttpServletRequest request,
        final HttpServletResponse response
    ) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}