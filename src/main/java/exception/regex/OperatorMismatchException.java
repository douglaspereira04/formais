package exception.regex;

public class OperatorMismatchException extends Exception {

	private static final long serialVersionUID = 2573039199136807300L;

	public OperatorMismatchException(String errMessage) {
		super(errMessage);
	}

	public OperatorMismatchException() {
		super("Operator Mismatch");
	}

}
