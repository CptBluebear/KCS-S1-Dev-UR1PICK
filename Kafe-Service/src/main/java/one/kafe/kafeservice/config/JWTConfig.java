package one.kafe.kafeservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import one.kafe.kafeservice.auth.jwt.AuthTokenProvider;
import one.kafe.kafeservice.auth.jwt.AuthTokenProviderImpl;

@Configuration
public class JWTConfig {

	@Value("${jwt.secretKey}")
	private String secretKey;

	@Bean
	public AuthTokenProvider authTokenProvider() {
		return new AuthTokenProviderImpl(secretKey);
	}

}
