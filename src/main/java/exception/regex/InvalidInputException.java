package exception.regex;

public class InvalidInputException extends Exception {

	private static final long serialVersionUID = 2573039199136807300L;

	public InvalidInputException(String errMessage) {
		super(errMessage);
	}

	public InvalidInputException() {
		super("Invalid Input");
	}
}
