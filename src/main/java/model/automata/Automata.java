package model.automata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;

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
    private MultiKeyMap<String, List<String>> transitions = null;

    public Automata() {
	super();
	this.states = new ArrayList<String>();
	this.finalStates = new ArrayList<String>();
	this.transitions = new MultiKeyMap<String, List<String>>();
    }

    public Automata(char lexeme) {
	super();
	this.states = new ArrayList<String>();
	this.finalStates = new ArrayList<String>();
	this.transitions = new MultiKeyMap<String, List<String>>();

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

    public MultiKeyMap<String, List<String>> getTransitions() {
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
	}

	if (reachableStates.contains(targetState))
	    throw new DuplicatedTransitionException();

	reachableStates.add(targetState);
	transitions.put(sourceState, transitionCharacter.toString(), reachableStates);

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

	return transitions.get(sourceState, transitionCharacter.toString());

    }

    /**
     * Returns automata alphabet as character list of type List<Character>
     * 
     * @return
     */
    public List<Character> getAlphabet() {
	List<Character> alphabet = new ArrayList<Character>();

	for (Entry<MultiKey<? extends String>, List<String>> keys : transitions.entrySet()) {
	    Character symbol = keys.getKey().getKey(1).charAt(0);
	    if (!alphabet.contains(symbol))
		alphabet.add(symbol);
	}

	if (alphabet.size() == 0)
	    return null;

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
	    for (MultiKey<? extends String> keys : automata1.getTransitions().keySet()) {
		for (String targetState : automata1.getTransition(keys.getKey(0), keys.getKey(1).charAt(0))) {
		    unified.addTransition(keys.getKey(0) + "-1", keys.getKey(1).charAt(0), targetState + "-1");
		}
	    }

	    for (MultiKey<? extends String> keys : automata2.getTransitions().keySet()) {
		for (String targetState : automata2.getTransition(keys.getKey(0), keys.getKey(1).charAt(0))) {
		    unified.addTransition(keys.getKey(0) + "-2", keys.getKey(1).charAt(0), targetState + "-2");
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

	    for (MultiKey<? extends String> keys : automata1.getTransitions().keySet()) {
		for (String targetState : automata1.getTransition(keys.getKey(0), keys.getKey(1).charAt(0))) {
		    concat.addTransition(keys.getKey(0) + "-1", keys.getKey(1).charAt(0), targetState + "-1");
		}
	    }

	    for (MultiKey<? extends String> keys : automata2.getTransitions().keySet()) {
		for (String targetState : automata2.getTransition(keys.getKey(0), keys.getKey(1).charAt(0))) {
		    concat.addTransition(keys.getKey(0) + "-2", keys.getKey(1).charAt(0), targetState + "-2");
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

	    for (MultiKey<? extends String> keys : automata.getTransitions().keySet()) {
		for (String targetState : automata.getTransition(keys.getKey(0), keys.getKey(1).charAt(0))) {
		    closure.addTransition(keys.getKey(0) + "-c", keys.getKey(1).charAt(0), targetState + "-c");
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

}
