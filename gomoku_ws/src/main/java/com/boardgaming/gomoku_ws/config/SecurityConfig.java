package com.boardgaming.gomoku_ws.config;

import com.boardgaming.core.config.auth.filter.JwtFilter;
import com.boardgaming.core.config.auth.handler.JwtAccessDeniedHandler;
import com.boardgaming.core.config.auth.handler.JwtAuthenticationEntryPointHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
			.headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.logout(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.anyRequest().authenticated())
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(exceptionHandling -> exceptionHandling
				.authenticationEntryPoint(authenticationEntryPointHandler)
				.accessDeniedHandler(accessDeniedHandler))
			.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
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