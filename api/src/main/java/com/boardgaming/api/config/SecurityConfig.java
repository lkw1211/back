package com.boardgaming.api.config;

import com.boardgaming.api.auth.command.CustomOAuth2UserService;
import com.boardgaming.api.auth.handler.OAuth2FailureHandler;
import com.boardgaming.api.auth.handler.OAuth2SuccessHandler;
import com.boardgaming.api.auth.repository.CookieAuthorizationRequestRepository;
import com.boardgaming.core.config.auth.filter.JwtFilter;
import com.boardgaming.core.config.auth.handler.JwtAccessDeniedHandler;
import com.boardgaming.core.config.auth.handler.JwtAuthenticationEntryPointHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	@Value("${server.allowedOrigins}")
	private List<String> allowedOrigins;
	private final JwtFilter jwtFilter;
	private final JwtAccessDeniedHandler accessDeniedHandler;
	private final JwtAuthenticationEntryPointHandler authenticationEntryPointHandler;
	private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	private final OAuth2FailureHandler oAuth2FailureHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
			.headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
			.headers(headers -> headers.addHeaderWriter((request, response) -> {
					response.setHeader("Cross-Origin-Embedder-Policy", "credentialless");
					response.setHeader("Cross-Origin-Resource-Policy", "same-site");
			}))
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.logout(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				.requestMatchers(HttpMethod.GET, "/api/auth").authenticated()
				.requestMatchers("/api/auth/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/user/check/**").permitAll()
				.requestMatchers(HttpMethod.PATCH, "/api/user/change/password").permitAll()
				.anyRequest().authenticated())
			.oauth2Login(oauth2 -> oauth2
				.authorizationEndpoint(endpointConfig -> endpointConfig.baseUri("/oauth2/call")
					.authorizationRequestRepository(cookieAuthorizationRequestRepository))
					.redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig.baseUri("/oauth2/callback/*"))
				.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
				.successHandler(oAuth2SuccessHandler)
				.failureHandler(oAuth2FailureHandler))
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(exceptionHandling -> exceptionHandling
					.authenticationEntryPoint(authenticationEntryPointHandler)
					.accessDeniedHandler(accessDeniedHandler))
			.build();
	}

	@Bean
	public UrlBasedCorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedHeaders(List.of("*"));
		corsConfig.setAllowedMethods(Arrays.asList("GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH".split(",")));
		corsConfig.setAllowedOriginPatterns(
			allowedOrigins
		);

		corsConfig.setExposedHeaders(List.of("*"));
		corsConfig.setAllowCredentials(true);
		corsConfig.setMaxAge(3600L);
		corsConfigSource.registerCorsConfiguration("/**", corsConfig);

		return corsConfigSource;
	}
}