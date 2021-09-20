package model.regex;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import exception.automata.DuplicatedStateException;
import exception.automata.DuplicatedTransitionException;
import exception.automata.InvalidStateException;
import exception.regex.BracketMismatchException;
import exception.regex.InvalidInputException;
import exception.regex.OperatorMismatchException;
import model.automata.Automata;
import model.automata.Transition;

@Ignore
public class RegexTest {
	
	String[] regexes = new String[] {"(a|b)*abb", " ", "a", "", "a|b", "a*", "a**", "(a|b|c|d|e)*"};

    @Test
    public void regexToAutomata() throws BracketMismatchException, OperatorMismatchException, InvalidInputException, InvalidStateException, DuplicatedStateException, DuplicatedTransitionException {
    	
    	Regex regex = null;
    	for (int k = 0; k < regexes.length; k++) {
			regex = new Regex(regexes[k]);
			Automata auto = regex.convert();
			System.out.println("REGEX TO AUTOMATA: ");
			System.out.println("ESTADOS:");
		    List<String> states = auto.getStates();
		    for (int i = 0; i < states.size(); i++)
		    	System.out.println(states.get(i));
		    System.out.println("ESTADO INICIAL");
		    System.out.println(auto.getInitialState());
		    System.out.println("ESTADO FINAIS:");
		    List<String> finalStates2 = auto.getFinalStates();
		    for (int i = 0; i < finalStates2.size(); i++)
		    	System.out.println(finalStates2.get(i));
		    System.out.println("TRANSITIONS:");
			for (Transition transition : auto.getTransitions()) {
				for (String targetState : transition.getTargetStates()) {
					System.out.println(transition.getSourceState() + ">>" +transition.getTransitionCharacter()+">>"+ targetState);
				}
			}
			System.out.println("--------------");
		}
    }

}
