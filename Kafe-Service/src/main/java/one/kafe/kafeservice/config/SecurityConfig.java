package one.kafe.kafeservice.config;

import java.io.OutputStream;
import java.util.List;

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
import one.kafe.kafeservice.auth.filter.TokenAuthFilter;
import one.kafe.kafeservice.auth.jwt.AuthTokenProvider;
import one.kafe.kafeservice.type.dto.ResponseModel;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthTokenProvider authTokenProvider;

	private static final String[] PERMIT_ALL = {
		"/v2/api-docs",
		"/swagger-resources",
		"/swagger-resources/**",
		"/configuration/ui",
		"/configuration/security",
		"/swagger-ui.html",
		"/webjars/**",
		"/v3/api-docs/**",
		"/swagger-ui/**",
		"/",
		"/api/shorturl"
	};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.formLogin().disable()
			.csrf().disable()
			.cors().configurationSource(corsConfigurationSource())
			.and()
			.addFilterBefore(tokenAuthFilter(), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests().antMatchers(PERMIT_ALL).permitAll()
			.and()
			.authorizeRequests()
			.anyRequest().authenticated();

		httpSecurity.exceptionHandling()
			.authenticationEntryPoint((request, response, authException) -> {
				authException.printStackTrace();
				ResponseModel responseModel = ResponseModel.builder()
					.httpStatus(HttpStatus.UNAUTHORIZED)
					.message("인증되지 않은 사용자이거나 권한이 부족합니다.")
					.build();
				response.setStatus(401);
				response.setContentType("application/json");
				OutputStream outputStream = response.getOutputStream();
				outputStream.write(responseModel.toJson().getBytes());
			});

		return httpSecurity.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();

		corsConfiguration.setAllowedOriginPatterns(List.of("*"));
		corsConfiguration.setAllowedMethods(List.of("*"));
		corsConfiguration.setAllowedHeaders(List.of("*"));
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
