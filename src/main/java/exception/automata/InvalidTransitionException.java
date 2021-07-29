package exception.automata;

public class InvalidTransitionException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = -2578329070324763299L;

    public InvalidTransitionException(String message) {
	super(message);
    }

    public InvalidTransitionException() {
	super("Invalid transition");
    }
}