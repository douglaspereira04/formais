package model.automata;

import java.util.List;

/**
 * POJO dedicated to represent a transition
 * @author douglas
 *
 */
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
