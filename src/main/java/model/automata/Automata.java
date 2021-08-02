package model.automata;

import java.util.ArrayList;
import java.util.List;

import exception.automata.DuplicatedStateException;
import exception.automata.DuplicatedTransitionException;
import exception.automata.InvalidStateException;

/**
 * Finite automata class
 * 
 * @author thiago
 * @author douglas
 *
 */
public class Automata {

	/**
	 * States list
	 */
	private List<String> states = null;

	/**
	 * Initial state
	 */
	private String initialState = null;

	/**
	 * Final states
	 */
	private List<String> finalStates = null;

	/**
	 * Transition table - A MultiKeyMap with: "source state" as first key;
	 * "transition character" as second key; "reachable states" list of
	 * {@code List<String>} as values;
	 * 
	 */
	private List<Transition> transitions = null;

	public Automata() {
		super();
		this.states = new ArrayList<String>();
		this.finalStates = new ArrayList<String>();
		this.transitions = new ArrayList<Transition>();
	}

	public Automata(char lexeme) {
		super();
		this.states = new ArrayList<String>();
		this.finalStates = new ArrayList<String>();
		this.transitions = new ArrayList<Transition>();

		String initial = "initial";
		try {
			this.addState(initial);
			this.setInitialState(initial);
			String finalS = Character.toString(lexeme);
			this.addState(finalS);
			this.setAsFinalState(finalS);
			this.addTransition(initial, lexeme, finalS);
		} catch (DuplicatedStateException e) {
			System.err.println("Construtor");
			e.printStackTrace();
		} catch (InvalidStateException e) {
			System.err.println("Construtor");
			e.printStackTrace();
		} catch (DuplicatedTransitionException e) {
			System.err.println("Construtor");
			e.printStackTrace();
		}

	}

	public List<String> getStates() {
		return states;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	/**
	 * Set a state as final state
	 * 
	 * @param state
	 * @throws InvalidStateException
	 */
	public void setAsFinalState(String state) throws InvalidStateException {
		if (states == null)
			throw new IllegalStateException();

		if (!states.contains(state))
			throw new InvalidStateException();

		this.finalStates.add(state);
	}

	public List<String> getFinalStates() {
		return finalStates;
	}

	/**
	 * Get initial state
	 */
	public String getInitialState() {
		return initialState;
	}

	/**
	 * Set state as initial
	 * 
	 * @param state
	 */
	public void setInitialState(String initialState) {
		this.initialState = initialState;
	}

	/**
	 * Adds state to automata
	 * 
	 * @param name - State name
	 * @throws AutomataException When automata named as parameter {@code name} is
	 *                           already in automata
	 */
	public void addState(String state) throws DuplicatedStateException {
		if (states == null)
			throw new IllegalStateException();

		if (states.contains(state))
			throw new DuplicatedStateException();

		states.add(state);
	}

	/**
	 * 
	 * @param sourceState         - transition source state
	 * @param transitionCharacter - transition character
	 * @param targetState         - transition reach state
	 * @throws InvalidStateException         When refers to states not present in
	 *                                       automata
	 * @throws DuplicatedTransitionException
	 */
	public void addTransition(String sourceState, Character transitionCharacter, String targetState)
			throws InvalidStateException, DuplicatedTransitionException {
		if (transitions == null)
			throw new IllegalStateException();

		if (!states.contains(sourceState))
			throw new InvalidStateException();

		if (!states.contains(targetState))
			throw new InvalidStateException();

		List<String> reachableStates = getTransition(sourceState, transitionCharacter);

		if (reachableStates == null) {
			reachableStates = new ArrayList<String>();
			transitions.add(new Transition(sourceState, transitionCharacter, reachableStates));
		}

		if (reachableStates.contains(targetState))
			throw new DuplicatedTransitionException();

		reachableStates.add(targetState);

	}

	/**
	 * Returns reachable states list of type {@code List<String>} given a source
	 * state and a transition character <br>
	 * returns <b>{@code null}</b> if transition does not exist
	 * 
	 * @param sourceState         - transition source state
	 * @param transitionCharacter - transition character
	 * @throws InvalidStateException When refers to states not present in automata
	 */
	public List<String> getTransition(String sourceState, Character transitionCharacter) throws InvalidStateException {
		if (transitions == null)
			throw new IllegalStateException();

		if (!states.contains(sourceState))
			throw new InvalidStateException();

		for (Transition transition : this.transitions) {
			if (transition.getSourceState().equals(sourceState)
					&& transition.getTransitionCharacter().equals(transitionCharacter))
				return transition.getTargetStates();
		}
		return null;

	}

	/**
	 * Returns automata alphabet as character list of type List<Character>
	 * 
	 * @return
	 */
	public List<Character> getAlphabet() {
		List<Character> alphabet = new ArrayList<Character>();

		for (Transition transition : transitions) {
			Character symbol = transition.getTransitionCharacter();
			if (!alphabet.contains(symbol))
				alphabet.add(symbol);
		}

		if (alphabet.size() == 0)
			alphabet = null;

		return alphabet;
	}

	/**
	 * Completes transition with dead state
	 * 
	 * @param String dead - dead state name
	 * @throws DuplicatedStateException      - in case dead state already existis
	 * @throws InvalidStateException
	 * @throws DuplicatedTransitionException
	 */
	public void complete(String dead)
			throws DuplicatedStateException, InvalidStateException, DuplicatedTransitionException {

		if (states == null)
			throw new IllegalStateException();

		if (states.contains(dead))
			throw new DuplicatedStateException();

		states.add(dead);

		for (Character character : getAlphabet()) {
			addTransition(dead, character, dead);
		}

		for (String state : states) {
			for (Character character : getAlphabet()) {
				if (getTransition(state, character) == null)
					addTransition(state, character, dead);
			}
		}
	}

	public static Automata unify(Automata automata1, Automata automata2) {

		String initial = "initial";
		Automata unified = new Automata();

		List<String> states1 = automata1.getStates();
		List<String> finalStates1 = automata1.getFinalStates();

		List<String> states2 = automata2.getStates();
		List<String> finalStates2 = automata2.getFinalStates();

		try {
			unified.addState(initial);
			unified.setInitialState(initial);

			for (String state : states1) {
				unified.addState(state + "-1");
			}

			for (String state : finalStates1) {
				unified.setAsFinalState(state + "-1");
			}

			for (String state : states2) {
				unified.addState(state + "-2");
			}

			for (String state : finalStates2) {
				unified.setAsFinalState(state + "-2");
			}

		} catch (DuplicatedStateException e) {
			e.printStackTrace();
		} catch (InvalidStateException e) {
			e.printStackTrace();
		}

		try {
			for (Transition transition : automata1.getTransitions()) {
				for (String targetState : transition.getTargetStates()) {
					unified.addTransition(transition.getSourceState() + "-1", transition.getTransitionCharacter(),
							targetState + "-1");
				}
			}

			for (Transition transition : automata2.getTransitions()) {
				for (String targetState : transition.getTargetStates()) {
					unified.addTransition(transition.getSourceState() + "-2", transition.getTransitionCharacter(),
							targetState + "-2");
				}
			}

			unified.addTransition(initial, '&', automata1.getInitialState() + "-1");
			unified.addTransition(initial, '&', automata2.getInitialState() + "-2");
		} catch (DuplicatedTransitionException e) {
			e.printStackTrace();
		} catch (InvalidStateException e) {
			e.printStackTrace();
		}

		return unified;
	}

	public static Automata concat(Automata automata1, Automata automata2) {

		String initial = "initial";
		Automata concat = new Automata();
		try {
			concat.addState(initial);
			concat.setInitialState(initial);

			List<String> states1 = automata1.getStates();
			List<String> finalStates1 = automata1.getFinalStates();

			List<String> states2 = automata2.getStates();
			List<String> finalStates2 = automata2.getFinalStates();

			for (String state : states1) {
				concat.addState(state + "-1");
			}

			for (String state : states2) {
				concat.addState(state + "-2");
			}

			for (Transition transition : automata1.getTransitions()) {
				for (String targetState : transition.getTargetStates()) {
					concat.addTransition(transition.getSourceState() + "-1", transition.getTransitionCharacter(),
							targetState + "-1");
				}
			}

			for (Transition transition : automata2.getTransitions()) {
				for (String targetState : transition.getTargetStates()) {
					concat.addTransition(transition.getSourceState() + "-2", transition.getTransitionCharacter(),
							targetState + "-2");
				}
			}

			concat.addTransition(initial, '&', automata1.getInitialState() + "-1");

			for (String state : finalStates1) {
				concat.addTransition(state + "-1", '&', automata2.getInitialState() + "-2");
			}

			for (String state : finalStates2) {
				concat.setAsFinalState(state + "-2");
			}

		} catch (DuplicatedStateException e) {
			e.printStackTrace();
		} catch (InvalidStateException e) {
			e.printStackTrace();
		} catch (DuplicatedTransitionException e) {
			e.printStackTrace();
		}

		return concat;
	}

	public static Automata closure(Automata automata) {

		String initial = automata.getInitialState() + "-c";
		Automata closure = new Automata();

		List<String> states = automata.getStates();
		List<String> finalStates = automata.getFinalStates();

		try {
			for (String state : states) {
				closure.addState(state + "-c");
			}

			for (String state : finalStates) {
				closure.setAsFinalState(state + "-c");
				closure.addTransition(state + "-c", '&', initial);
			}

			closure.setInitialState(initial);
			closure.setAsFinalState(initial);

			for (Transition transition : automata.getTransitions()) {
				for (String targetState : transition.getTargetStates()) {
					closure.addTransition(transition.getSourceState() + "-c", transition.getTransitionCharacter(),
							targetState + "-c");
				}
			}

		} catch (DuplicatedStateException e) {
			e.printStackTrace();
		} catch (InvalidStateException e) {
			e.printStackTrace();
		} catch (DuplicatedTransitionException e) {
			e.printStackTrace();
		}

		return closure;
	}

	/**
	 * Gets epsilon closure of a given state
	 * 
	 * @param state to get epsilon closure
	 * @return epsilon closure of a given state as {@code List<String>}
	 * @throws InvalidStateException
	 */
	public List<String> getEpsilonClosure(String state) throws InvalidStateException {
		if (!states.contains(state))
			throw new InvalidStateException();

		List<String> closure = new ArrayList<String>();
		closure.add(state);

		getEpsilonClosure(state, closure);

		return closure;

	}

	/**
	 * Stores epsilon closure of a given state into closure list
	 * 
	 * @param state   String
	 * @param closure of type {@code List<String>}
	 * @throws InvalidStateException
	 */
	public void getEpsilonClosure(String state, List<String> closure) throws InvalidStateException {

		List<String> epsilonTransition = getTransition(state, '&');
		for (String targetState : epsilonTransition) {
			if (!closure.contains(targetState)) {
				closure.add(targetState);
				getEpsilonClosure(targetState, closure);
			}
		}

	}

	public Automata determinize() {

		return null;
	}

	public Automata minimize() {

		return null;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	public class Transition {
		private String sourceState = null;
		private Character transitionCharacter = null;
		private List<String> targetStates = null;

		public Transition(String sourceState, Character transitionCharacter, List<String> targetState) {
			super();
			this.sourceState = sourceState;
			this.transitionCharacter = transitionCharacter;
			this.targetStates = targetState;
		}

		public String getSourceState() {
			return sourceState;
		}

		public void setSourceState(String sourceState) {
			this.sourceState = sourceState;
		}

		public Character getTransitionCharacter() {
			return transitionCharacter;
		}

		public void setTransitionCharacter(Character transitionCharacter) {
			this.transitionCharacter = transitionCharacter;
		}

		public List<String> getTargetStates() {
			return targetStates;
		}

		public void setTargetStates(List<String> targetStates) {
			this.targetStates = targetStates;
		}
	}

}
