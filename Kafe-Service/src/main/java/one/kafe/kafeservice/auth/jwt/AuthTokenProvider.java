package one.kafe.kafeservice.auth.jwt;

import java.util.Date;

import org.springframework.security.core.Authentication;

public interface AuthTokenProvider {
	AuthToken createToken(String id, String role, Date expiry);

	AuthToken convertToken(String token);

	Authentication getAuthentication(AuthToken authToken);
}
