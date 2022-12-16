package one.kafe.kafeservice.exception;

public class NotAllowUrlException extends Exception {

	private final static String MESSAGE = "Not Allow URL Pattern!";

	public NotAllowUrlException() {
		super(MESSAGE);
	}

	public NotAllowUrlException(String message) {
		super(message);
	}

	public NotAllowUrlException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAllowUrlException(Throwable cause) {
		super(MESSAGE, cause);
	}
}
