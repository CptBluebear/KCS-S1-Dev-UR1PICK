package one.kafe.kafeservice.exception;

public class SearchResultNotExistException extends Exception {

	private final static String MESSAGE = "Search Result Not Exist!";

	public SearchResultNotExistException() {
		super(MESSAGE);
	}

	public SearchResultNotExistException(String message) {
		super(message);
	}

	public SearchResultNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public SearchResultNotExistException(Throwable cause) {
		super(MESSAGE, cause);
	}
}
