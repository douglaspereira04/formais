package exception.automata;

/**
 * 
 * @author douglas
 *
 */
public class InvalidStateException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2578329070324763299L;

	public InvalidStateException(String message) {
		super(message);
	}

	public InvalidStateException() {
		super("Invalid state");
	}
}