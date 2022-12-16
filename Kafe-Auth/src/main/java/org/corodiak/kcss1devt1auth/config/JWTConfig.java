package org.corodiak.kcss1devt1auth.config;

import org.corodiak.kcss1devt1auth.auth.jwt.AuthTokenProvider;
import org.corodiak.kcss1devt1auth.auth.jwt.AuthTokenProviderImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConfig {

	@Value("${jwt.secretKey}")
	private String secretKey;

	@Bean
	public AuthTokenProvider authTokenProvider() {
		return new AuthTokenProviderImpl(secretKey);
	}

}
