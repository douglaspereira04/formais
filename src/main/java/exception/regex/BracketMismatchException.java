package exception.regex;

public class BracketMismatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2573039199136807300L;

	public BracketMismatchException(String errMessage) {
		super(errMessage);
	}

	public BracketMismatchException() {
		super("Bracket Mismatch");
	}

}
