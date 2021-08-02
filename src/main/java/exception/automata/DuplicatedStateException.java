package exception.automata;

public class DuplicatedStateException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2578329070324763299L;

	public DuplicatedStateException(String message) {
		super(message);
	}

	public DuplicatedStateException() {
		super("Duplicated state");
	}
}