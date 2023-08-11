package com.boardgaming.core.config.resolver;

import com.boardgaming.core.config.annotation.LoginUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public String resolveArgument(
        final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) {
        UserDetails principal = (UserDetails)SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();

        return principal.getUsername();
    }
}
