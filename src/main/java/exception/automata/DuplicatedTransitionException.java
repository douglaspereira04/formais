package exception.automata;

public class DuplicatedTransitionException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = -2578329070324763299L;

    public DuplicatedTransitionException(String message) {
	super(message);
    }

    public DuplicatedTransitionException() {
	super("Duplicated transition");
    }
}