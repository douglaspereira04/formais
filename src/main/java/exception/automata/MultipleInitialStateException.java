package exception.automata;

/**
 * 
 * @author douglas
 *
 */
public class MultipleInitialStateException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2578329070324763299L;

	public MultipleInitialStateException(String message) {
		super(message);
	}

	public MultipleInitialStateException() {
		super("Multiple initial states");
	}
}