package model.regex;

import java.util.List;

import javax.swing.UIManager;

import org.junit.Test;

import com.formdev.flatlaf.FlatLightLaf;

import exception.regex.BracketMismatchException;
import exception.regex.InvalidInputException;
import exception.regex.OperatorMismatchException;
import model.automata.Automata;
import model.automata.Automata.Transition;

public class RegexTest {

    @Test
    public void regexTest() {
	try {
	    Regex re = new Regex("(a(a|b)*ba)");
	    System.out.println(re.getRegex());
	    System.out.println(re.concatOp(re.getRegex()));
	    System.out.println(re.infixToPostfix(re.concatOp(re.getRegex())));
	    Automata aut = re.convert();
	    System.out.println("ESTADOS:");
	    List<String> states = aut.getStates();
	    for (int i = 0; i < states.size(); i++)
		System.out.println(states.get(i));
	    System.out.println("ESTADO INICIAL");
	    System.out.println(aut.getInitialState());
	    System.out.println("ESTADO FINAIS:");
	    List<String> finalStates = aut.getFinalStates();
	    for (int i = 0; i < finalStates.size(); i++)
		System.out.println(finalStates.get(i));
	    System.out.println("TRANSI��ES:");
		for (Transition transition : aut.getTransitions()) {
			for (String targetState : transition.getTargetStates()) {
				System.out.println(transition.getSourceState() + ">>" +transition.getTransitionCharacter()+">>"+ targetState);
			}
		}

	} catch (BracketMismatchException e) {
	    e.printStackTrace();
	} catch (OperatorMismatchException e) {
	    e.printStackTrace();
	} catch (InvalidInputException e) {
	    e.printStackTrace();
	}

	try {
	    UIManager.setLookAndFeel(new FlatLightLaf());
	} catch (Exception ex) {
	    System.err.println("Failed to initialize LaF");
	}
    }

}
