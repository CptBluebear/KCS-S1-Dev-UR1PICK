package org.corodiak.kcss1devt1auth.exception;

import org.springframework.security.core.AuthenticationException;

public class EmailDuplicateException extends AuthenticationException {
	private static final String DEFAULT_MESSAGE = "Duplicated Email";

	public EmailDuplicateException() {
		super(DEFAULT_MESSAGE);
	}

	public EmailDuplicateException(String msg) {
		super(msg);
	}

	public EmailDuplicateException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

	public EmailDuplicateException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
