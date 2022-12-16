package org.corodiak.kcss1devt1auth.auth.jwt;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class AuthToken {

	@Getter
	private final String token;

	private final Key key;

	AuthToken(String id, String role, Date expiry, Key key) {
		this.key = key;
		token = createToken(id, role, expiry);
	}

	private String createToken(String id, String role, Date expiry) {
		return Jwts.builder()
			.setHeaderParam("typ", "JWT")
			.setSubject("token")
			.claim("id", id)
			.claim("role", role)
			.setIssuedAt(new Date())
			.setExpiration(expiry)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public boolean validate() {
		return this.getClaims() != null;
	}

	public Claims getClaims() {
		Claims claims = null;
		try {
			claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException ignored) {
			log.debug("Auth Token is Not Validated. : {}", token);
		}
		return claims;
	}
}