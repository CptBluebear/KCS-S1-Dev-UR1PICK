package org.corodiak.kcss1devt1auth.config;

import java.io.OutputStream;
import java.util.List;

import org.corodiak.kcss1devt1auth.auth.filter.TokenAuthFilter;
import org.corodiak.kcss1devt1auth.auth.handler.OAuth2AuthenticationFailureHandler;
import org.corodiak.kcss1devt1auth.auth.handler.OAuth2AuthenticationSuccessHandler;
import org.corodiak.kcss1devt1auth.auth.jwt.AuthTokenProvider;
import org.corodiak.kcss1devt1auth.auth.service.CustomOAuth2UserService;
import org.corodiak.kcss1devt1auth.type.dto.ResponseModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final AuthTokenProvider authTokenProvider;

	private static final String[] PERMIT_ALL = {
		"/login/**",
		"/api/verify",
		"/api/signup",
		"/api/signin",
		"/api/test",
		"/"
	};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.oauth2Login()
			.userInfoEndpoint()
			.userService(customOAuth2UserService)
			.and()
			.successHandler(oAuth2AuthenticationSuccessHandler())
			.failureHandler(oAuth2AuthenticationFailureHandler())
			.and()
			.authorizeRequests()
			.antMatchers(PERMIT_ALL).permitAll()
			.and()
			.authorizeRequests()
			.anyRequest().authenticated();

		httpSecurity.csrf().disable();

		httpSecurity.cors().configurationSource(corsConfigurationSource());

		httpSecurity.exceptionHandling()
			.authenticationEntryPoint((request, response, authException) -> {
				ResponseModel responseModel = ResponseModel.builder()
					.httpStatus(HttpStatus.UNAUTHORIZED)
					.message("인증되지 않은 사용자이거나 권한이 부족합니다.")
					.build();
				response.setStatus(401);
				response.setContentType("application/json");
				OutputStream outputStream = response.getOutputStream();
				outputStream.write(responseModel.toJson().getBytes());
			});

		httpSecurity.addFilterBefore(tokenAuthFilter(), UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();

	}

	@Bean
	public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
		return new OAuth2AuthenticationSuccessHandler(authTokenProvider);
	}

	@Bean
	public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
		return new OAuth2AuthenticationFailureHandler();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();

		corsConfiguration.setAllowedOriginPatterns(List.of("*"));
		corsConfiguration.setAllowedMethods(List.of("*"));
		corsConfiguration.setAllowedHeaders(List.of("*"));
		corsConfiguration.setExposedHeaders(List.of("*"));
		corsConfiguration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);

		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public TokenAuthFilter tokenAuthFilter() {
		return new TokenAuthFilter(authTokenProvider);
	}
}
