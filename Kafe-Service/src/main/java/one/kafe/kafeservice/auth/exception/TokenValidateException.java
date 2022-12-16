package one.kafe.kafeservice.auth.exception;

public class TokenValidateException extends RuntimeException {
	public TokenValidateException() {
		super("Fail to validate Token");
	}
}
