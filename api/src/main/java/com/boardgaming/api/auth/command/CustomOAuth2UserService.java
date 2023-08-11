package com.boardgaming.api.auth.command;

import com.boardgaming.domain.auth.domain.OAuth2Attributes;
import com.boardgaming.domain.auth.domain.OAuth2CustomUser;
import com.boardgaming.domain.auth.domain.OAuth2Provider;
import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;


    @Override
    @Transactional
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);

        Map<String, Object> originAttributes = new HashMap<>(oAuth2User.getAttributes());
        OAuth2Provider provider = OAuth2Provider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2Attributes attributes = OAuth2Attributes.of(provider, originAttributes);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(Role.USER.getAuthority()));

        String userId = getUserId(provider, Objects.requireNonNull(attributes));

        if (userId.equals("isEmpty")) {
            originAttributes.put("oAuth2Attributes", attributes);
        }

        return new OAuth2CustomUser(originAttributes, authorities, userId);
    }

    private String getUserId(
        final OAuth2Provider provider,
        final OAuth2Attributes authAttributes
    ) {
        return userRepository.findByEmailAndProvider(authAttributes.getEmail(), provider)
            .map(User::getId)
            .orElse("isEmpty");
    }
}
