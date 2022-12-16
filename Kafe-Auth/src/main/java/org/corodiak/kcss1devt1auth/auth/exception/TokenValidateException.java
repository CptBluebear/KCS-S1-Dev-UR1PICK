package org.corodiak.kcss1devt1auth.auth.exception;

public class TokenValidateException extends RuntimeException {
	public TokenValidateException() {
		super("Fail to validate Token");
	}
}
