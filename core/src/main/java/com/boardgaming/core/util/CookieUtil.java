package com.boardgaming.core.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

@UtilityClass
public class CookieUtil {
    private final Jackson2JsonRedisSerializer<String> serializer = new Jackson2JsonRedisSerializer<>(String.class);

    public Optional<Cookie> getCookie(
        final HttpServletRequest request,
        final String name
    ) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public void addCookie(
        final HttpServletResponse response,
        final String name,
        final Object value,
        final long maxAge,
        final String domain,
        final String sameSite
    ) {
        response.addHeader("Set-Cookie", ResponseCookie.from(name, serialize(value))
            .domain(domain)
            .path("/")
            .maxAge(maxAge)
            .httpOnly(true)
            .secure(true)
            .sameSite(sameSite)
            .build()
            .toString());
    }

    public void deleteCookie(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final String name,
        final String cookieDomain,
        final String sameSite
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    ResponseCookie responseCookie = ResponseCookie.from(name, "")
                        .path("/")
                        .maxAge(0)
                        .domain(cookieDomain)
                        .secure(true)
                        .sameSite(sameSite)
                        .build();
                    response.addHeader("Set-Cookie", responseCookie.toString());
                }
            }
        }
    }

    public <T> T deserialize(
        final Cookie cookie,
        final Class<T> clazz
    ) {
        return clazz.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }

    public String serialize(final Object object) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
    }
}
